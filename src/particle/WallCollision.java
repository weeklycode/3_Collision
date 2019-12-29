package particle;

public class WallCollision implements Collision{
	public enum Type{
		TOP,
		BOT,
		LEFT,
		RIGHT;
	}

	private double time;
	private Type t;
	public WallCollision(Type t, double timeUntil){
		this.t=t;
		time=timeUntil;
	}

	public Type getType(){
		return t;
	}

	@Override
	public void updateTime(double change) {
		time-=change;
	}

	@Override
	public double getTimeUntil() {
		return time;
	}


}
