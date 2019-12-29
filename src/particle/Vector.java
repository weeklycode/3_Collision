package particle;

public class Vector {

	public final double x, y;

	public Vector(double x, double y){
		this.x=x;
		this.y=y;
	}

	public Vector unit(){
		double size = Math.sqrt(x*x + y*y);
		return new Vector(x/size, y/size);
	}

	public Vector scale(double by){
		return new Vector(x*by, y*by);
	}

	public Vector plus(Vector v){
		return new Vector(x+v.x, y+v.y);
	}

	public Vector minus(Vector v){
		return new Vector(x-v.x, y-v.y);
	}

	public double dot(Vector v){
		return x*v.x + y*v.y;
	}
}
