import java.awt.*;

public class Bullet extends Sprite2D {
	public Bullet(Image i) {
		super(i, i);
		
	}
	
	public boolean move() {
		y -= 10;
		return (y<0);
	}
	
	
}
