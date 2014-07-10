/**
 * @author Louis Orleans
 * @date 10 September 2012
*/

package dudeofawesome.spaceZombies;

//import java.awt.image.BufferedImage;
//import java.io.File;
//import javax.imageio.ImageIO;
//import java.io.IOException;
//
public class powerup{
	private final int LASER = 0;
	private final int HEALTH = 1;
	private final int NUKE = 2;
	private final int TWOSHOT = 3;
	private final int SHOTGUN = 4;
	private final int SHIELD = 5;

	public int type = LASER;
	public int x,y,diameter = 20,life = 1000,alive;
//	public BufferedImage sprite;
//	powerup(int _type,int _x,int _y){
//		type = _type;
//		x = _x;
//		y = _y;
//
//		switch(type){
//			case LASER:
//				try{
//					sprite = ImageIO.read(getClass().getResource("images/laser.png"));
//				}
//				catch (IOException ex){
//
//				}
//			break;
//			case HEALTH:
//				try{
//					sprite = ImageIO.read(getClass().getResource("images/health.png"));
//				}
//				catch (IOException ex){
//
//				}
//			break;
//			case NUKE:
//				try{
//					sprite = ImageIO.read(getClass().getResource("images/nuke.png"));
//				}
//				catch (IOException ex){
//
//				}
//			break;
//			case TWOSHOT:
//				try{
//					sprite = ImageIO.read(getClass().getResource("images/twoshot.png"));
//				}
//				catch (IOException ex){
//
//				}
//			break;
//			case SHOTGUN:
//				try{
//					sprite = ImageIO.read(getClass().getResource("images/shotgun.png"));
//				}
//				catch (IOException ex){
//
//				}
//			break;
//			case SHIELD:
//				try{
//					sprite = ImageIO.read(getClass().getResource("images/shield.png"));
//				}
//				catch (IOException ex){
//
//				}
//			break;
//		}
//	}
	public boolean move(){
		if(alive > life){
			return false;
		}
		else{
			alive++;
			return true;
		}
	}
}