package gameBrickBreaker;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener { //JPanel is a part of java swing package
	private boolean play=false; //player currently playing or not
	private int score =0;  //initial score
	
	private int totalBricks=32;  //total bricks
	private Timer timer;
	private int delay=8;	//speed of the ball
	private int playerX=310; //starting position of slider
	private int ballposX=120;	//starting position of ball
	private int ballposY=350;
	private int ballXdir=-1;	//initial direction of ball
	private int ballYdir=-2;
	private MapGenerator map; // it is used from map generator
	
	public Gameplay()	//Constructor
	{
		
		map=new MapGenerator(4,8);//(row,column)
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
	}
	
	public void paint(Graphics g) //method paint
	{
		//background
		g.setColor(Color.pink);
		g.fillRect(1, 1, 692, 592);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//drawing map
		map.draw((Graphics2D) g);
		
		
		
		//scores
		g.setColor(Color.blue);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString(""+score, 590, 30);
		
		// the paddle
		g.setColor(Color.black);
		g.fillRect(playerX, 540, 100, 8);
		
		
		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 30, 30);
		
		if(totalBricks <= 0) // for printing you won and restart
		{
			play=false;
			ballXdir= 0;
			ballYdir= 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Hurrah! You Won,Score:"+score, 290, 300);
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		if(ballposY > 570) //for printing game over and restart
		{
			play=false;
			ballXdir=0;
			ballYdir=0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("GameOver,Score:"+score,290,300);
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart",230,350);
		}
		g.dispose(); 
	}

	@Override
	public void actionPerformed(ActionEvent e) { //method override from interfaces
		
		timer.start();
		
		if(play)
		{
			if(new Rectangle(ballposX,ballposY,30,30).intersects(new Rectangle(playerX,550,100,8))) //intersection of ball and slider
			{
				ballYdir = -ballYdir;
			}
		A:  for(int i=0;i<map.map.length;i++)
			{
				for(int j=0;j<map.map[0].length;j++)
				{
					if(map.map[i][j]>0)
					{
						int brickX=j*map.brickWidth+100;
						int brickY=i*map.brickHeight+70;
						int brickWidth=map.brickWidth;
						int brickHeight=map.brickHeight;
						Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
						Rectangle ballRect= new Rectangle(ballposX,ballposY,30,30);
						Rectangle brickRect=rect;
						if(ballRect.intersects(brickRect))
						{
							map.setBrickValue(0,i,j);
							totalBricks--;
							
							score +=10;
							if(ballposX+19<=brickRect.x||ballposX+1>=brickRect.x+brickRect.width)
							{
								ballXdir = -ballXdir;
							}
							else
							{
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
				}
			}
			ballposX+=ballXdir;
			ballposY+=ballYdir;
			if(ballposX < 0)
			{ 
				ballXdir = -ballXdir;   //left
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;   //top
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir; //Right
			}
		}
		repaint();
		
	}

	@Override
	public void keyTyped(KeyEvent e) { //method override from interfaces
		
	}

	@Override
	public void keyReleased(KeyEvent e) { //method override from interfaces
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) { //method override from interfaces
		
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) //click on right arrow the slider moves right
		{
			if(playerX >= 600)
			{
				playerX = 600;
			}
		}
		else
		{
			moveRight();
		}
		if(e.getKeyCode()==KeyEvent.VK_LEFT) //click on left arrow the slider moves left
		{
			if(playerX < 10)
			{
				playerX = 10;
			}
		}
		else
		{
			moveLeft();
		}
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if(play)
			{
				play=true;
				ballposX=120;
				ballposY=350;
				ballXdir=-1;
				ballYdir=-2;
				playerX=310;
				score=0;
				totalBricks=32;
				
				map=new MapGenerator(4,8); //rows and columns
				repaint();
			}
		}
		
	}
	
	public void moveRight()
	{
		play=true;
		playerX+=30;
	}
	public void moveLeft()
	{
		play=true;
		playerX-=30;
	}
}
