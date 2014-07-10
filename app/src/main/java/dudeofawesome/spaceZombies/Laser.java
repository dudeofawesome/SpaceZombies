/**
 * @author Louis Orleans
 * @date 10 September 2012
*/

package dudeofawesome.spaceZombies;

import android.graphics.Color;

public class Laser{
	int x1;
	int y1;
	int x2;
	int y2;
	int alive = 0;
	int life = 200;
	int length = 999; 
	int width = 1;
	int color = Color.RED;

	Laser(int _x1,int _y1,int _x2,int _y2){
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
	}
	Laser(int _x1,int _y1,int _x2,int _y2,int _length){
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		length = _length;
	}
	public void move(int _x1,int _y1,int _x2,int _y2,int _width,int _height){
		length = (int) Math.hypot(_width,_height);

		double _angle = Math.atan2((_y2 - _y1),(_x2 - _x1));
		_x2 = (int) (Math.cos(_angle) * length) + _x1;
		_y2 = (int) (Math.sin(_angle) * length) + _y1;

		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;

		width = (int) (Math.random() * 3 + 1);
	}
}