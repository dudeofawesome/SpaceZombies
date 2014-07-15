/**
 * @author Louis Orleans
 * @date 10 September 2012
*/

package dudeofawesome.spaceZombies;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    Resources res = WorkoutTimer.context.getResources();
    Bitmap sprite;

	powerup(int _type,int _x,int _y){
		type = _type;
		x = _x;
		y = _y;

		switch(type){
			case LASER:
                sprite = BitmapFactory.decodeResource(res, R.drawable.laser);
			break;
			case HEALTH:
                sprite = BitmapFactory.decodeResource(res, R.drawable.health);
            break;
			case NUKE:
                sprite = BitmapFactory.decodeResource(res, R.drawable.nuke);
            break;
			case TWOSHOT:
                sprite = BitmapFactory.decodeResource(res, R.drawable.twoshot);
            break;
			case SHOTGUN:
                sprite = BitmapFactory.decodeResource(res, R.drawable.shotgun);
            break;
			case SHIELD:
                sprite = BitmapFactory.decodeResource(res, R.drawable.shield);
            break;
		}
	}
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