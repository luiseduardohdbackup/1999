
//Java Classes
import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;
import java.applet.Applet;
import java.util.ArrayList;
import java.util.Random;

//Impulse Engine Classes
import com.impulse.graphics.ImageLoader;
import com.impulse.graphics.Animation;
import com.impulse.graphics.Actor;
import com.impulse.geometry.Vector2;
import com.impulse.event.Event;
import com.impulse.event.EventManager;

public class Game1999 extends Applet implements Runnable, MouseListener, MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image dbImage;
	private Graphics dbg;
	private int baseX = 0;
	private int baseY = -3600;
	private int rowNumber = 8;

	private ArrayList<Image> tiles;
	private ArrayList<Actor> actors;
	private ImageLoader imageLoader;
	
	private Plane[] enemy;
	private Plane plane;
	private Bullet[] bullets;
	private int nextBullet;
	
	//Animiations
	private	Animation animationPlane;
	private	Animation animationEnemy;
	private	Animation animationExplosion;
	private	Animation animationBullet;
	
	//Events
	private EventManager eventManager;
	private AddEnemy1 eventAddEnemy1;
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//	APPLET FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////			
		
	public void init(){
		this.resize(800, 600);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void start(){
	
		hideMouse();
	
		dbImage = createImage(this.getSize().width, this.getSize().height);
		dbg = dbImage.getGraphics();
				
		loadImages();
		loadEvents();
		loadTiles();
		loadSprites();
		
		Thread t = new Thread(this);
		t.start();
	}
	
	public void stop(){}
	public void destroy(){}
			
	public void run(){
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while(true){
			repaint();
			try{
				Thread.sleep(20);
			}catch(InterruptedException e){
			}
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}
			
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//	MOUSE FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void mouseEntered(MouseEvent e){}
   	public void mouseExited(MouseEvent e){}
  	public void mouseClicked(MouseEvent e){}
	
   	public void mousePressed(MouseEvent e){
		boolean canShoot = false;
		for(int i = 0; i <= 10; i++){
			if(!bullets[i].isActive()){
				canShoot = true;
			}
		}
		if(canShoot){
			bullets[nextBullet].isActive(true);
			bullets[nextBullet].setLocation(new Vector2.Integer(e.getX() + bullets[nextBullet].getXOffset(), e.getY() + bullets[nextBullet].getYOffset()));
			if(nextBullet < 10){
				nextBullet++;
			}else{
				nextBullet = 0;
			}
		}
   	}
	
   	public void mouseReleased(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
	
	public void mouseMoved(MouseEvent e){
		plane.setLocation(new Vector2.Integer(e.getX(), e.getY()));
	}
	
	private void hideMouse(){
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisiblecursor");
		setCursor(transparentCursor);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//	LOADING FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void loadImages(){
		imageLoader = new ImageLoader(this);
		imageLoader.loadImage("../../images/water_basic.gif");
		//imageLoader.loadImage("land_basic.gif");
		//imageLoader.loadImage("mountains_basic.gif");
		imageLoader.loadImage("../../images/environment_1.gif");
		imageLoader.loadImage("../../images/environment_2.gif");
		imageLoader.loadImage("../../images/environment_3.gif");
		imageLoader.loadImage("../../images/plane1.gif");
		imageLoader.loadImage("../../images/plane2.gif");
		imageLoader.loadImage("../../images/plane3.gif");
		imageLoader.loadImage("../../images/bullets_small.gif");
		imageLoader.loadImage("../../images/bullets_one.gif");
		imageLoader.loadImage("../../images/bullets_big.gif");
		imageLoader.loadImage("../../images/bullets_two.gif");
		imageLoader.loadImage("../../images/enemy1_1.gif");
		imageLoader.loadImage("../../images/enemy1_2.gif");
		imageLoader.loadImage("../../images/enemy1_3.gif");
		imageLoader.loadImage("../../images/explosion_1.gif");
		imageLoader.loadImage("../../images/explosion_2.gif");
		imageLoader.loadImage("../../images/explosion_3.gif");
		imageLoader.loadImage("../../images/explosion_4.gif");
		imageLoader.loadImage("../../images/explosion_5.gif");
		imageLoader.loadImage("../../images/explosion_6.gif");
		imageLoader.loadImage("../../images/explosion_7.gif");
	}
		
	private void loadEvents(){
	
		eventManager = new EventManager();
	
		eventAddEnemy1 = new AddEnemy1();
		eventAddEnemy1.isActive(true);
		
		eventManager.addEvent(eventAddEnemy1);
	}
	
	private void loadTiles(){
		tiles = new ArrayList<Image>();
		Random generator = new Random();
		for(int i = 0; i < 960; i++){
			int j = generator.nextInt(1000);
			if(j <= 900){
				tiles.add(imageLoader.getImage("../../images/water_basic.gif"));
			}else if(j <= 950){
				tiles.add(imageLoader.getImage("../../images/environment_1.gif"));
			}else if(j <= 975){
				tiles.add(imageLoader.getImage("../../images/environment_2.gif"));
			}else if(j <= 1000){
				tiles.add(imageLoader.getImage("../../images/environment_3.gif"));
			}
		}
	}
	
	private void loadSprites(){
	
		actors = new ArrayList<Actor>();
			
		plane = new Plane(dbg, this, 0, 0, 50, 50);
		plane.isActive(true);
		plane.addAnimation(new AnimationPlane());
		plane.addAnimation(new AnimationExplosion1());
		actors.add(plane);
				
		bullets = new Bullet[11];
		for(int i = 0; i <= 10; i++){
			bullets[i] = new Bullet(dbg, this, 0, 0, 10, 10);
			bullets[i].addAnimation(new AnimationBullet());
			bullets[i].setSpeed(new Vector2.Integer(0, -15));
			if(i % 2 == 0){
				bullets[i].setLevel(0);
			}else{
				bullets[i].setLevel(3);
			}
			actors.add(bullets[i]);
		}
		
		enemy = new Plane[5];
		for(int i = 0; i < 5; i++){
			enemy[i] = new Plane(dbg, this, 0, 0, 50, 50);
			enemy[i].isActive(true);
			enemy[i].setLocation(new Vector2.Integer(100 * (i + 1), 0));
			enemy[i].setSpeed(new Vector2.Integer(0, 2));
			enemy[i].addAnimation(new AnimationEnemy1());
			enemy[i].addAnimation(new AnimationExplosion1());
			actors.add(enemy[i]);
		}
	
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//	RENDER FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void update(Graphics g){
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
		dbg.setColor(getForeground());
		paint(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void paint(Graphics g){
		
		drawTiles();
		
		for(Actor a : actors){
			if(a.isActive()){
				a.update();
				a.render();
				if(a instanceof Bullet){
					checkBulletCollisions((Bullet)a);
					if(a.getLocation().getY() <= -20){
						a.isActive(false);
					}
				}else if(a == plane){
					checkEnemyCollisions();
				}
			}
		}
		
		eventManager.runEvents();
		
		//g.drawString("Row = " + rowNumber, 20, 20);
			
	}
			
	private void drawTiles(){
		int x = baseX;
		int y = baseY;
		int rows = 80;
		int rowDrawCount = 0;
		for(int j = 0; j < 960; j++){
			Image i = tiles.get(j);
			if(y >= -50 && y <= 600){
				if(rowDrawCount == 0){
					rowNumber = (j + 1) / 12;
				}
				dbg.drawImage(i, x, y, this);
				rowDrawCount++;
			}
			x += 50;
			if(x == 600){
				y += 50;
				x = 0;
			}
			rows--;
		}
		if(baseY < 0){
			baseY += 1;
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//	LOGIC FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	//This checks to see if a bullet hits an enemy
	//if it does it will call the enemy event listner and destroy the bullet
	private void checkBulletCollisions(Bullet b){
		for(Actor a : actors){
			if(a instanceof Plane && a != plane && a.isActive()){
				if(a.checkCollision(b)){
					b.isActive(false);
				}
			}
		}
	}
	
	//This checks to see if an enemy collides with the plane
	private void checkEnemyCollisions(){
		for(Actor a : actors){
			if(a instanceof Plane && a != plane){
				plane.checkCollision(a);
			}
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//	ANIMATIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////		
	
	class AnimationPlane extends Animation{
		public AnimationPlane(){
			addFrame(0, imageLoader.getImage("../../images/plane1.gif"));
			addFrame(1, imageLoader.getImage("../../images/plane2.gif"));
			addFrame(2, imageLoader.getImage("../../images/plane3.gif"));
		}
	}
	
	class AnimationBullet extends Animation{
		public AnimationBullet(){
			addFrame(0, imageLoader.getImage("../../images/bullets_small.gif"));
			addFrame(1, imageLoader.getImage("../../images/bullets_one.gif"));
			addFrame(2, imageLoader.getImage("../../images/bullets_big.gif"));
			addFrame(3, imageLoader.getImage("../../images/bullets_two.gif"));
		}
	}
	
	class AnimationEnemy1 extends Animation{
		public AnimationEnemy1(){
			addFrame(0, imageLoader.getImage("../../images/enemy1_1.gif"));
			addFrame(1, imageLoader.getImage("../../images/enemy1_2.gif"));
			addFrame(2, imageLoader.getImage("../../images/enemy1_3.gif"));
		}
	}
	
	class AnimationExplosion1 extends Animation{
		public AnimationExplosion1(){
			addFrame(0, imageLoader.getImage("../../images/explosion_1.gif"));
			addFrame(1, imageLoader.getImage("../../images/explosion_1.gif"));
			addFrame(2, imageLoader.getImage("../../images/explosion_2.gif"));
			addFrame(3, imageLoader.getImage("../../images/explosion_2.gif"));
			addFrame(4, imageLoader.getImage("../../images/explosion_3.gif"));
			addFrame(5, imageLoader.getImage("../../images/explosion_3.gif"));
			addFrame(6, imageLoader.getImage("../../images/explosion_4.gif"));
			addFrame(7, imageLoader.getImage("../../images/explosion_4.gif"));
			addFrame(8, imageLoader.getImage("../../images/explosion_5.gif"));
			addFrame(9, imageLoader.getImage("../../images/explosion_5.gif"));
			addFrame(10, imageLoader.getImage("../../images/explosion_6.gif"));
			addFrame(11, imageLoader.getImage("../../images/explosion_6.gif"));
			addFrame(12, imageLoader.getImage("../../images/explosion_7.gif"));
			addFrame(13, imageLoader.getImage("../../images/explosion_7.gif"));
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//	EVENTS
//////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	class AddEnemy1 extends Event{
		public void run(){
			for(int i = 0; i < 5; i++){
				enemy[i].isActive(true);
				enemy[i].getLocation().setY(-50);
			}
		}
		public boolean condition(){
			return rowNumber == 65;
		}
	}
		
}