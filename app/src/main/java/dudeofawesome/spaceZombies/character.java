/**
 * @author Louis Orleans
 * @date 10 September 2012
 */

package dudeofawesome.spaceZombies;

import android.graphics.Color;
import java.util.ArrayList;

public class character {
    public int x,y,diameter,totalV,oldX,oldY;
    public int color;
    public int health = 100;
    public ArrayList<Integer> collectedPowerups = new ArrayList<Integer>();

    character(int _xLoc,int _yLoc,int _diam,int _totalV,int _color){
        x = _xLoc;
        y = _yLoc;
        diameter = _diam;
        totalV = _totalV;
        color = _color;
    }

    public void move(int _targetX,int _targetY){
        oldX = x;
        oldY = y;

        double _velX, _velY;
        double _angleTo;
        _angleTo = (double) Math.atan2((_targetY - y),(_targetX - x));

        //split it up into x and y
        _velX = (int) (Math.cos(_angleTo) * totalV);
        _velY = (int) (Math.sin(_angleTo) * totalV);

        x += _velX;
        y += _velY;
    }

    public void moveBack(){
        x = oldX;
        y = oldY;
    }
}
