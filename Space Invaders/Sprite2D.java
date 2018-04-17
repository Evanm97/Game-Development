import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Sprite2D {
	
	//member data
	
	protected double xSpeed = 0, ySpeed = 0;
	protected Image myImage;
	protected Image myImage2;
	protected double x, y;
	protected static int winWidth;
	protected int framesDrawn;
	
	//constructor
	public Sprite2D(Image i, Image j) {
		myImage = i;
		myImage2 = j;
		
	}
	
	public void setPosition(double xx, double yy) {
		x=xx;
		y=yy;
	}
	
	public void setXSpeed(double dx) {
		xSpeed=dx;
	}
	
	public void setYSpeed(double dy) {
		ySpeed=dy;
	}
	
	public void paint(Graphics g) {
		framesDrawn++;
		if ( framesDrawn%100<50 )
		g.drawImage(myImage, (int)x, (int)y, null);
		else
		g.drawImage(myImage2, (int)x, (int)y, null);
		
	}
	
	public static void setWinWidth(int w) {
		winWidth = w;
	}
}
