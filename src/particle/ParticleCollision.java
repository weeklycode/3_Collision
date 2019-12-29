package particle;

public class ParticleCollision implements Collision{

	private Particle p;
	private double time;

	public ParticleCollision(Particle p, double timeUntil){
		this.p=p;
		time=timeUntil;
	}

	public Particle getParticle(){
		return p;
	}

	@Override
	public void updateTime(double change){
		time-=change;
	}

	@Override
	public double getTimeUntil(){
		return time;
	}

}
