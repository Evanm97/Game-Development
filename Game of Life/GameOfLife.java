import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JFrame;

public class GameOfLife extends JFrame implements Runnable, MouseListener, MouseMotionListener {
	
	public static final Dimension WindowSize = new Dimension(800,800);
	private BufferStrategy strategy;
	private Graphics offscreenBuffer;
	public int GRIDSIZE = 40;
	private boolean playing = false;
	private String FilePath;
	private int gameStateFrontBuffer = 0;
	private boolean initialised = false;

	public Boolean gameState[][][] = new Boolean[GRIDSIZE][GRIDSIZE][2];
	
	public GameOfLife() {
		this.setTitle("Conway's Game of Life");
	    Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width/2 - 400;
		int y = screensize.height/2 - 400;
		setBounds(x, y, 800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		FilePath = System.getProperty("user.dir") + "\\";
    	System.out.println("Working Directory = " + FilePath);
		
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		offscreenBuffer = strategy.getDrawGraphics();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		for(x=0; x<40; x++) {
			for(y=0; y<40; y++) {
				gameState[x][y][0] = gameState[x][y][1] = false;
			}
		}
		
		Thread t = new Thread(this);
		t.start();
		initialised = true;

	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
        		}   
			catch (InterruptedException e) { 
				
			}
			if(playing){
				int front = gameStateFrontBuffer;
				int back = (front+1)%2;
				
				for(int x=0; x<40; x++) {
					for(int y=0; y<40; y++) {
						int neighbours = 0;
						for(int xx=-1; xx<=1; xx++) {
							for(int yy=-1; yy<=1; yy++) {
								if(xx!=0 || yy!=0) {
									int X = x+xx;
									int Y = y+yy;
									if(X<0)
										X=0;
										if(X>=40)
											X=39;
											if(Y<0)
												Y=0;
												if(Y>=40)
													Y=39;
													if(gameState[X][Y][front] == true)
														neighbours++;
								}
							}
						}
						if(neighbours<2 && gameState[x][y][front] == true){
							gameState[x][y][back]=false;
						}
						else if(neighbours>3 && gameState[x][y][front] == true){
							gameState[x][y][back]=false;
						}
						else if(neighbours==3 && gameState[x][y][front] == false){
							gameState[x][y][back] = true;
						}
					}
				}
				for (int x=0; x<40; x++) {
					for (int y=0; y<40; y++) {
						gameState[x][y][front]=gameState[x][y][back];
					}
				}
			}
			this.repaint();
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if(!playing){
			int x = e.getX();
			int y = e.getY();
			if((x>=15 && x<=85) && (y>=40 && y<=65)){											
				playing = true;
			}
			if((x>=90 && x<=160) && (y>=40 && y<=65)){
				randomiseGameState();
			}
			
			if((x>=165 && x<=235) && (y>=40 && y<=65)){
				saveFile();
			}
			
			if((x>=240 && x<=310) && (y>=40 && y<=65)){
				loadFile();
			}
			
			
			x = e.getX()/20;
			y = e.getY()/20;
		
			gameState[x][y][gameStateFrontBuffer] = !gameState[x][y][gameStateFrontBuffer];
			
			this.repaint();
		}
		}

	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void mouseClicked(MouseEvent e) {
	
	}

	public void mouseEntered(MouseEvent e) {
	
	}

	public void mouseExited(MouseEvent e) {
		
	}
	
	int prevx = -1; int prevy = -1;
	public void mouseDragged(MouseEvent e) {
		int x = e.getX()/20;
		int y = e.getY()/20;
		
		if(x!= prevx || y!= prevy) {
			gameState[x][y][gameStateFrontBuffer] = !gameState[x][y][gameStateFrontBuffer];
			this.repaint();
			prevx = x;
			prevy = y;
		}
	}

	public void mouseMoved(MouseEvent arg0) {
		
	}
	
	private void writeString(Graphics g, int x, int y, int fontSize, String message) {
	    Font f = new Font("Times", Font.PLAIN, fontSize);
	    g.setFont(f);
	    FontMetrics fm = getFontMetrics(f);
	    int width = fm.stringWidth(message);
	    g.drawString(message, x-width/2, y);
	}
	
	private void loadFile() {
		String filename = FilePath+"life.txt";
		String textinput = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			textinput = reader.readLine();
			reader.close();
		} 
		catch (IOException e) { }	
		
		if (textinput!=null) {
	        for (int x=0;x<40;x++) {
	        	for (int y=0;y<40;y++) {
	        		gameState[x][y][0] = (textinput.charAt(x*40+y)=='1');
	        	}
	        }			
		}
	}
	
	private void saveFile() {
		String outputtext="";
        for (int x=0;x<40;x++) {
        	for (int y=0;y<40;y++) {
        		if (gameState[x][y][0])
        			outputtext+="1";
        		else
        			outputtext+="0";
        	}
        }
        
		try {
			String filename = FilePath+"life.txt";
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(outputtext);
			writer.close();
		 } 
		catch (IOException e) { }		
	}
	
	public void randomiseGameState() {
		for (int x=0; x<40; x++) {
			for (int y=0; y<40; y++) {
				gameState[x][y][gameStateFrontBuffer]=(Math.random()<0.25);
				}
		}
	}

	public void paint (Graphics g){
		if(!initialised)
			return;
		
        g = offscreenBuffer;
        
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);
        
        g.setColor(Color.WHITE);
		for (int x=0;x<40;x++) {
			for (int y=0;y<40;y++) {
				if (gameState[x][y][gameStateFrontBuffer]) {
					g.fillRect(x*20, y*20, 20, 20);
				}
			}
		}
       
        if(!playing) {
        	g.setColor(Color.green);
        	g.fillRect(15, 40, 70, 25);
        	g.fillRect(90, 40, 70, 25);
        	g.fillRect(165, 40, 70, 25);
			g.fillRect(240, 40, 70, 25);
        	g.setColor(Color.white);
        	writeString(g,50,60,20,"START");
        	writeString(g,125,60,20,"RAND");
        	writeString(g,200,60,20,"SAVE");
        	writeString(g,275,60,20,"LOAD");
        }
    
        strategy.show();
        }

	public static void main(String[] args) {
		GameOfLife g = new GameOfLife();
	}
	
}
