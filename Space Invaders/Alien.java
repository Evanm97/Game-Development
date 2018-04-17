import java.awt.*;import java.awt.Image;
import java.awt.Graphics;

public class Alien extends Sprite2D {
	private static double xSpeed = 0;
	protected boolean alive = true;
	
	public Alien(Image i, Image j) {
		super(i, j);

	}
	
	public void paint(Graphics g) {
		if(alive)
			super.paint(g);
	}
	
	public boolean move() {
		x += xSpeed;
		
		if(x<=0 || x>=winWidth-myImage.getWidth(null))
			return true;
		else
			return false;
	}
	
	public static void setFleetXSpeed(double dx) {
		xSpeed = dx;
	}
	public static void reverseDirection() {
		xSpeed = -xSpeed;
	
	}
	
	public void jumpDownwards() {
		y+=20;
	 }
}
