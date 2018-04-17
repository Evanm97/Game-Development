import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.Iterator;
import java.util.ArrayList;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

	//member data
	private static boolean isGraphicsInitialised = false;
	public static final Dimension WindowSize = new Dimension(800,600);
	private Graphics offscreenBuffer;
	private boolean isInitialised;
	private static final int NUMALIENS = 30;
	private Alien[] AliensArray = new Alien[NUMALIENS];
	private Ship PlayerShip;
	private Image bulletImage;
	private ArrayList bulletsList = new ArrayList();
	private BufferStrategy strategy;
	public int score = 0;
	public int enemyWave = 1;
	public boolean isGameInProgress = false;
	public int highscore = 0;
	public int fleetSpeed =2;
	
	//constructor
	public InvadersApplication() {
		this.setTitle("SPACE INVADERS");
	    Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width/2 - WindowSize.width/2;
		int y = screensize.height/2 - WindowSize.height/2;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//Alien.setFleetXSpeed(fleetSpeed);
		
		ImageIcon icon = new ImageIcon("C:\\Users\\user\\OneDrive\\Documents\\GD Week 3\\alien_ship_1.png");
		Image alienImage = icon.getImage();
		ImageIcon icon2 = new ImageIcon("C:\\Users\\user\\OneDrive\\Documents\\GD Week 3\\alien_ship_2.png");
		Image alienImage2 = icon2.getImage();
		
		ImageIcon icon3 = new ImageIcon("C:\\Users\\user\\OneDrive\\Documents\\GD Week 3\\bullet.png");
		Image bulletImage = icon3.getImage();
		bulletImage = bulletImage.getScaledInstance(5, 30, Image.SCALE_DEFAULT);
		
		// ship images
		ImageIcon icon4 = new ImageIcon("C:\\Users\\user\\OneDrive\\Documents\\GD Week 3\\ship.png");
		Image shipImage = icon4.getImage();
		shipImage = shipImage.getScaledInstance(80, 80, Image.SCALE_DEFAULT);
		PlayerShip = new Ship(shipImage, bulletImage);
		
		for (int i=0; i<NUMALIENS; i++) {
			AliensArray[i] = new Alien(alienImage, alienImage2);
		}
		
		Sprite2D.setWinWidth(WindowSize.width);
		
		Thread t = new Thread(this);
		t.start();

		addKeyListener(this);
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		offscreenBuffer = strategy.getDrawGraphics();
		isInitialised = true;
	}
	//threads entry point
	public void run() {
        while (true) {
        
              try {
                  Thread.sleep(20);
               }   catch (InterruptedException e) { }
          	
              if(isGameInProgress) {
            	  boolean anyAliensAlive = false;
          	 	  boolean alienDirectionReversalNeeded = false;
          	 	  for (int i = 0; i<NUMALIENS; i++) {
          	 		  if (AliensArray[i].alive) {
          	 			  anyAliensAlive = true;
          	 		  if (AliensArray[i].move())
          	 			  alienDirectionReversalNeeded = true;
            	  
            	  if(isCollision(PlayerShip.x,AliensArray[i].x,PlayerShip.y,AliensArray[i].y,54,50,32,32) )
            	  {
            		  isGameInProgress = false;
            	  }
            	 }
              }
              
          	 	  if (alienDirectionReversalNeeded) {
            	  	Alien.reverseDirection();
            	  	for(int i = 0; i<NUMALIENS; i++) 
                	  	AliensArray[i].jumpDownwards();
              }
              
              if(!anyAliensAlive) {
            	  fleetSpeed +=2;
            	  startNewWave();
              }
             
              PlayerShip.move();
              
              Iterator iterator = bulletsList.iterator();
              while(iterator.hasNext()){
              Bullet b = (Bullet) iterator.next();
              if(b.move()) {
            	  iterator.remove();
              }
              else {
            	  double x2 = b.x, y2 = b.y;
            	  double w1 = 50, h1 = 32;
            	  double w2 = 6, h2 = 16;
            	  for(int i = 0; i < NUMALIENS; i++) {
            		  if(AliensArray[i].alive) 
            		  {
            			  double x1 = AliensArray[i].x;
            			  double y1 = AliensArray[i].y;
            			  	if(isCollision(x1,x2,y1,y2,w1,w2,h1,h2)) 
            			  	{
            				  AliensArray[i].alive = false;
            				  iterator.remove();
            				  score += 10;
            				  if(score > highscore)
            					  highscore = score;
            	
            				  break;
            			  	}
            		  }
            	  }
               }
            }
          }
              	this.repaint();
            }
          }
      
	
	public void startNewWave() {
		Alien.setFleetXSpeed(fleetSpeed);
		for(int i=0; i<NUMALIENS; i++) {
    		double xx = (i%5)*80 + 70;
    		double yy = (i/5)*40 + 50;
    		AliensArray[i].setPosition(xx, yy);
    		AliensArray[i].setXSpeed(fleetSpeed);
    		AliensArray[i].alive =true;
    		AliensArray[i].framesDrawn = 0;
    		AliensArray[i].move();
    	}
	}
	
	public void startNewGame() {
		fleetSpeed = 2;
		score = 0;
		isGameInProgress = true;
		startNewWave();
		PlayerShip.setPosition(375, 510);
	}
	
	
	private boolean isCollision(double x1, double x2, double y1, double y2, double w1, double w2, double h1, double h2)
	{
		if(((x1<x2 && x1+w1>x2) || (x2<x1 && x2+w2>x1)) 
				&& ((y1<y2 && y1+h1>y2) || (y2<y1 && y2+h2>y1)))
			return true;
		else
			return false;
	}

	public void keyPressed(KeyEvent e){
		if(isGameInProgress) {
			if(e.getKeyCode()==KeyEvent.VK_LEFT)
				PlayerShip.setXSpeed(-4);
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
				PlayerShip.setXSpeed(4);
			else if(e.getKeyCode() == e.VK_SPACE)
		bulletsList.add(PlayerShip.shootBullet());
		}
		else {
			startNewGame();
		}
	}
		        
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT)
			PlayerShip.setXSpeed(0);
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
    private void writeString(Graphics g, int x, int y, int fontSize, String message) {
    	Font f = new Font("Times", Font.PLAIN, fontSize);
    	g.setFont(f);
    	FontMetrics fm = getFontMetrics(f);
    	int width = fm.stringWidth(message);
    	g.drawString(message, x-width/2, y);
    }
	public void paint (Graphics g){
       
        if(!isInitialised)
        	return;
        
        g = offscreenBuffer;
        
        g.setColor(Color.black);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);
        
       if(isGameInProgress) {
        	for (Alien Alien : AliensArray) 
                Alien.paint(g);
        	
        	PlayerShip.paint(g);
        	
        	Iterator iterator = bulletsList.iterator();
            while(iterator.hasNext()){
            	Bullet b = (Bullet) iterator.next();
            	b.paint(g);
            	}
        	
        	g.setColor(Color.green);
        	writeString(g,650,60,20,"Score: "+score+   "     Highscore: "+highscore);
        }
       else{
        	g.setColor(Color.green);
        	writeString(g,WindowSize.width/2,200,60,"Space Invaders!");
        	g.setColor(Color.white);
        	writeString(g,WindowSize.width/2,300,30,"Press any key to play");
        	writeString(g,WindowSize.width/2,350,25,"[Arrow keys to move, Spacebar to fire]");
        }
       
        
      // g.dispose();
        strategy.show();
        }

    
	 
	public static void main(String[] args) {
		InvadersApplication i = new InvadersApplication();
	}
	
}
