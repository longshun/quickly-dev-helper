

public class Point {
	private float x;
	private float y;
	private int littlex;
	public int getLittlex() {
		return littlex;
	}
	public int getLittley() {
		return littley;
	}
	private int littley;
	public Point(float x,float y){
		this.x = x;
		this.y = y;
	}
	public Point(float x,float y,int lx,int ly){
		this.littlex = lx;
		this.littley = ly;
		this.x = x;
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
}
