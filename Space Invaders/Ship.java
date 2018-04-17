import java.awt.Image;

public class Ship extends Sprite2D {
	private Image bulletImage;

	public Ship(Image i, Image bullet) {
		super(i, i);
		bulletImage = bullet;
	}

	public void move() {
		x += xSpeed;
		if(x>=InvadersApplication.WindowSize.width-myImage.getWidth(null)) {
			x=InvadersApplication.WindowSize.width-myImage.getWidth(null);
			x -= xSpeed;
		}
		
		if(x<6) {
			x = 6;
			xSpeed = 0;
		}
	}
	
	public Bullet shootBullet() {
		Bullet b = new Bullet(bulletImage);
		b.setPosition(this.x+54/2 +11, this.y);
		return b;
	}
}
