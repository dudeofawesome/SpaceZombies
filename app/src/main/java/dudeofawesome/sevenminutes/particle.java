/**
 * @author Louis Orleans
 * @date 10 September 2012
*/

package dudeofawesome.sevenminutes;

import android.graphics.Color;

public class particle{

	private final int EXPLOSION = 0;
	private final int SMOKE = 1;
	private final int FIRE = 2;
	private final int EXHAUST = 3;
	private final int POWERUPDUST = 4;

	public int x = 0;
	public int y = 0;
	public double dir = 0;
	public double vel = 1;
	public int life = 100;
	public int alive = 0;
	public int diameter = 5;
	public int lineWidth = 3;
	public int type = EXPLOSION;
	public double randomness = .1;
	public int opacity = 255;
	public int color = Color.RED;
	public int[] colors = { Color.rgb(209,0,0), Color.rgb(219,29,0), Color.rgb(222,52,4), Color.rgb(232,99,5), Color.rgb(240,138,5), Color.rgb(252,179,20), Color.rgb(255,213,25), Color.rgb(255,255,46), Color.rgb(255,255,102), Color.rgb(255,255,176)};

	particle(int _type,int _x,int _y){
		//not type dependent settings
		x = _x;
		y = _y;

		type = _type;

		//type dependent settings
		if(type == EXPLOSION){
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 3 + .3;
			diameter = (int) (Math.random() * 5 + 5);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 100 + 140);
		}
		else if(type == SMOKE){
			colors[0] = Color.rgb(12,12,12);
			colors[1] = Color.rgb(20,20,20);
			colors[2] = Color.rgb(25,25,25);
			colors[3] = Color.rgb(30,30,30);
			colors[4] = Color.rgb(41,41,41);
			colors[5] = Color.rgb(60,60,60);
			colors[6] = Color.rgb(80,80,80);
			colors[7] = Color.rgb(100,100,100);
			colors[8] = Color.rgb(120,120,120);
			colors[9] = Color.rgb(148,148,148);
			color = colors[(int) (Math.random() * 9)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + 1;
			diameter = (int) (Math.random() * 10 + 5);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 20 + 100);
		}
		else if(type == FIRE){
			colors[0] = Color.rgb(214,39,0);
			colors[1] = Color.rgb(242,44,0);
			colors[2] = Color.rgb(242,69,0);
			colors[3] = Color.rgb(255,106,0);
			colors[4] = Color.rgb(255,157,0);
			colors[5] = Color.rgb(255,174,0);
			colors[6] = Color.rgb(255,179,0);
			colors[7] = Color.rgb(255,236,28);
			colors[8] = Color.rgb(255,245,135);
			colors[9] = Color.rgb(255,251,204);
			// color = colors[(int) (Math.random() * 9)];
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + .1;
			diameter = (int) (Math.random() * 4 + 3);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
		else if(type == EXHAUST){
			colors[0] = Color.rgb(55,0,100);
			colors[1] = Color.rgb(40,0,75);
			colors[2] = Color.rgb(33,0,60);
			colors[3] = Color.rgb(23,0,41);
			colors[4] = Color.rgb(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + .1;
			diameter = (int) (Math.random() * 3 + 2);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 8 + 93);
		}
		else if(type == POWERUPDUST){
			colors[0] = Color.rgb(55,0,100);
			colors[1] = Color.rgb(40,0,75);
			colors[2] = Color.rgb(33,0,60);
			colors[3] = Color.rgb(23,0,41);
			colors[4] = Color.rgb(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 4.0 + .1;
			diameter = (int) (Math.random() * 5 + 5);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
	}

	particle(int _type,int _x,int _y,int _vel){
		//not type dependent settings
		x = _x;
		y = _y;

		type = _type;

		//type dependent settings
		if(type == EXPLOSION){
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 5 + _vel;
			diameter = (int) (Math.random() * 5 + 5);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 100 + 140);
		}
		else if(type == SMOKE){
			colors[0] = Color.rgb(12,12,12);
			colors[1] = Color.rgb(20,20,20);
			colors[2] = Color.rgb(25,25,25);
			colors[3] = Color.rgb(30,30,30);
			colors[4] = Color.rgb(41,41,41);
			colors[5] = Color.rgb(60,60,60);
			colors[6] = Color.rgb(80,80,80);
			colors[7] = Color.rgb(100,100,100);
			colors[8] = Color.rgb(120,120,120);
			colors[9] = Color.rgb(148,148,148);
			// color = colors[(int) (Math.random() * 9)];
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 5 + _vel;
			diameter = (int) (Math.random() * 10 + 3);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 20 + 100);
		}
		else if(type == FIRE){
			colors[0] = Color.rgb(214,39,0);
			colors[1] = Color.rgb(242,44,0);
			colors[2] = Color.rgb(242,69,0);
			colors[3] = Color.rgb(255,106,0);
			colors[4] = Color.rgb(255,157,0);
			colors[5] = Color.rgb(255,174,0);
			colors[6] = Color.rgb(255,179,0);
			colors[7] = Color.rgb(255,236,28);
			colors[8] = Color.rgb(255,245,135);
			colors[9] = Color.rgb(255,251,204);
			// color = colors[(int) (Math.random() * 9)];
			color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 5.0 + _vel;
			diameter = (int) (Math.random() * 4 + 3);
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
		else if(type == EXHAUST){
			colors[0] = Color.rgb(55,0,100);
			colors[1] = Color.rgb(40,0,75);
			colors[2] = Color.rgb(33,0,60);
			colors[3] = Color.rgb(23,0,41);
			colors[4] = Color.rgb(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 2.0 + _vel;
			diameter = (int) (Math.random() * 3 + 3);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 8 + 93);
		}
		else if(type == POWERUPDUST){
			colors[0] = Color.rgb(55,0,100);
			colors[1] = Color.rgb(40,0,75);
			colors[2] = Color.rgb(33,0,60);
			colors[3] = Color.rgb(23,0,41);
			colors[4] = Color.rgb(16,0,29);
			color = colors[(int) (Math.random() * 5)];
			// color = colors[9];

			dir = Math.random() * 2.0 * Math.PI;
			vel = Math.random() * 2.0 + _vel;
			diameter = (int) (Math.random() * 5 + 5);
			alive = 70;
			lineWidth = diameter;
			opacity = (int) (Math.random() * 50 + 150);
		}
	}

	public boolean move(){
		if(alive > life){
			return false;
		}
		else{
			int _dX = (int) (Math.cos(dir) * vel);
			int _dY = (int) (Math.sin(dir) * vel);
			x += _dX;
			y += _dY;
			dir += Math.random() * 0.6 - 0.3;
			vel *= 0.99;
			if(opacity - 1 > 0){
				opacity --;
			}
			if(type == EXPLOSION || type == FIRE){
				switch(alive){
					case 10:
						color = colors[8];
					break;
					case 20:
						color = colors[7];
					break;
					case 30:
						color = colors[6];
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 40:
						color = colors[5];
					break;
					case 50:
						color = colors[4];
					break;
					case 60:
						color = colors[3];
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 70:
						color = colors[2];
					break;
					case 80:
						color = colors[1];
					break;
					case 90:
						color = colors[0];
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
				}
			}
			else{
				switch(alive){
					case 30:
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 60:
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
					case 90:
						if(lineWidth > 1){
							lineWidth -= 1;
						}
					break;
				}
			}

			alive ++;
			return true;
		}
	}
}