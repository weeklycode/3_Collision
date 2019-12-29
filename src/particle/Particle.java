package particle;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import particle.WallCollision.Type;

public class Particle {

	private Vector pos, vel;
	private double radius, mass;
	private List<Particle> actors;
	private LinkedList<Collision> colls;
	private Color color;

	public Particle(Vector pos, Vector vel, double radius, List<Particle> actors, Color color){
		this.pos=pos;
		this.vel=vel;
		this.radius=radius;
		this.mass=Math.PI*radius*radius;
		this.actors=actors;
		this.color=color;
		colls = new LinkedList<Collision>();
	}

	public String getT(){
		return colls.size()>0? String.format("%.0f",colls.get(0).getTimeUntil()) : "null";
	}

	public double getRadius(){
		return radius;
	}

	public Vector getPos(){
		return pos;
	}

	public void move(double timeframe){
		while (timeframe > 0){
			if (colls.size() == 0){
				pos = pos.plus(vel.scale(timeframe));
				return;
			}
			Collision collide = null;
			double timespent = timeframe;
			if (timeframe >= colls.get(0).getTimeUntil()){
				collide = colls.get(0);
				timespent = colls.get(0).getTimeUntil();
			}else{
				for (Collision c : colls){
					c.updateTime(timespent);
				}
			}
			timeframe-=timespent;
			pos = pos.plus(vel.scale(timespent));

			if (collide != null){
				if (collide instanceof ParticleCollision){
					collide(((ParticleCollision)collide).getParticle());
				}else if (collide instanceof WallCollision){
					wallCollide((WallCollision)collide);
				}else{
					throw new UnsupportedOperationException("Unknown Collision Type!");
				}
			}
		}
	}

	public void wallCollide(WallCollision c){
		switch((c).getType()){
		case TOP:
			vel=new Vector(vel.x, Math.abs(vel.y));
			break;
		case BOT:
			vel=new Vector(vel.x, -Math.abs(vel.y));
			break;
		case LEFT:
			vel=new Vector(Math.abs(vel.x), vel.y);
			break;
		case RIGHT:
			vel=new Vector(-Math.abs(vel.x), vel.y);
			break;
		default:
			throw new UnsupportedOperationException("Unknown WallCollision Type: "+c.getTimeUntil()+"!");
		}
		recalculate();
	}

	public void calcWalls(){
		double t;
		if (vel.x != 0){
			t = (Main.WIDTH-pos.x-radius)/vel.x;
			if (t > 0){
				add(new WallCollision(Type.RIGHT, t));
			}
			t = (radius-pos.x)/vel.x;
			if (t > 0){
				add(new WallCollision(Type.LEFT, t));
			}
		}
		if (vel.y != 0){
			t = (Main.HEIGHT-pos.y-radius)/vel.y;
			if (t > 0){
				add(new WallCollision(Type.BOT, t));
			}
			t = (radius-pos.y)/vel.y;
			if (t > 0){
				add(new WallCollision(Type.TOP, t));
			}
		}
	}

	public void collide(Particle other){
		Vector tempvel;
		Vector dx = pos.minus(other.pos);
		double scale = ((2*other.mass)/(mass+other.mass))*(vel.minus(other.vel).dot(dx)/(dx.dot(dx)));
		tempvel = vel.minus(dx.scale(scale));

		dx = other.pos.minus(pos);
		scale = ((2*mass)/(mass+other.mass))*(other.vel.minus(vel).dot(dx)/(dx.dot(dx)));
		other.vel = other.vel.minus(dx.scale(scale));

		vel = tempvel;

		recalculate();
		other.recalculate();
	}

	public Color getColor(){
		return color;
	}

	public void recalculate(){
		colls.clear();
		calcWalls();
		for (Particle p : actors){
			if (p == this){
				continue;
			}
			if (p.colls.contains(this)){
				p.colls.remove(this);
			}
			Vector dv = vel.minus(p.vel);
			double a = dv.dot(dv);

			if (a == 0){
				continue;
			}

			Vector dx = pos.minus(p.pos);
			double b = 2*dx.dot(dv);
			double r = radius+p.radius;
			double c = dx.dot(dx)-r*r;
			double d = b*b - 4*a*c;

			if (d<0){
				continue;
			}

			double time = (-b-Math.sqrt(d))/(2*a);
			if (time > 0){
				add(new ParticleCollision(p, time));
			}
		}
	}

	public void add(Collision c){
		if (colls.size() == 0){
			colls.add(c);
			return;
		}

		int left = 0, right = colls.size()-1;

		while (left < right){
			int mid = ((right+left)>>1);
			if (c.getTimeUntil() > colls.get(mid).getTimeUntil()){
				left = mid+1;
			}else{
				right = mid-1;
			}
		}
		if (c.getTimeUntil() > colls.get(left).getTimeUntil()){
			left++;
		}
		colls.add(left,c);
	}
}
