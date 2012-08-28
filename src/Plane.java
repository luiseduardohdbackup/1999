
//Java Imports
import java.awt.Graphics;
import java.applet.Applet;

//Impulse Engine Imports
import com.impulse.graphics.Actor;
import com.impulse.graphics.Sprite;
import com.impulse.event.CollisionListener;

public class Plane extends Sprite{
	
	public Plane(Graphics g, Applet a, int x, int y, int w, int h){
		super(g, a, x, y, w, h);
		addCollisionListener(new CollisionListener(){
			public void collisionEvent(Object... objects){
				setCurrentAnimation(1);
			}
		});
	}
						
	public void render(){
		getGraphics().drawImage(getCurrentAnimation().getCurrentFrame(), 
			(int)getLocation().getX(), (int)getLocation().getY(), getImageObserver());
		if(getCurrentAnimationIndex() == 1 && getCurrentAnimation().isLastFrame()){
			isActive(false);
			reset();
		}else{
			getCurrentAnimation().nextFrame();
		}
	}
			
	public void reset(){
		setCurrentAnimation(0);
		resetAnimations();
	}
	
}