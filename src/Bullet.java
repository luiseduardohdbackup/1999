
import java.awt.Graphics;
import java.applet.Applet;

import com.impulse.graphics.Sprite;

/**
 * The bullets that shoot from the plane
 * @author andrew
 *
 */
public class Bullet extends Sprite{

	private int level;
	
	public Bullet(Graphics g, Applet a, int x, int y, int w, int h){
		super(g, a, x, y, w, h);
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int l){
		level = l;
	}
	
	public int getXOffset(){
		if(level == 0){
			return 28;
		}else if(level == 1){
			return 25;
		}else if(level == 2){
			return 24;
		}else if(level == 3){
			return 21;
		}
		return 0;
	}
	
	public int getYOffset(){
		if(level == 0){
			return 16;
		}else if(level == 1){
			return 14;
		}else if(level == 2){
			return 5;
		}else if(level == 3){
			return 21;
		}
		return 0;
	}
			
	public void render(){
		getGraphics().drawImage(getCurrentAnimation().getFrame(level), 
			(int)getLocation().getX(), (int)getLocation().getY(), getImageObserver());
	}
			
}