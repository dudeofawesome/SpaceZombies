package dudeofawesome.spaceZombies;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.twicecircled.spritebatcher.Drawer;
import com.twicecircled.spritebatcher.FontParams;
import com.twicecircled.spritebatcher.SpriteBatcher;

import javax.microedition.khronos.opengles.GL10;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyRenderer extends GLSurfaceView implements Drawer {

    private final SpriteBatcher mRenderer;
    int[] resourceIDs = {R.drawable.particle, R.drawable.enemy, R.drawable.player, R.drawable.eightshot, R.drawable.health, R.drawable.laser, R.drawable.laser_fullsize, R.drawable.nuke, R.drawable.shield, R.drawable.shotgun, R.drawable.twoshot, R.string.audiowide_font};

    public MyRenderer (Context context) {
        super(context);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new SpriteBatcher(context, resourceIDs, this);
        mRenderer.setFontParams(R.string.audiowide_font, new FontParams().size(30));
        mRenderer.setMaxFPS(30);
        setRenderer(mRenderer);
    }

    public MyRenderer (Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new SpriteBatcher(context, resourceIDs, this);
        mRenderer.setFontParams(R.string.audiowide_font, new FontParams().size(30));
        mRenderer.setMaxFPS(30);
        setRenderer(mRenderer);
    }


    Rect src = new Rect(0,0,20,20);
    Rect dest = new Rect(0,0,20,20);

    @Override
    public void onDrawFrame(GL10 gl, SpriteBatcher sb) {
        src.set(0,0,20,20);
        dest.set(100, 100, 20, 20);

        //draw particles
        for(int i = 0; i < GameLoop.particles.size(); i++){
            if (GameLoop.particles.get(i) != null) {
                src.set(0, 0, 64, 64);
                dest.set(GameLoop.particles.get(i).x, GameLoop.particles.get(i).y, GameLoop.particles.get(i).x + GameLoop.particles.get(i).diameter, GameLoop.particles.get(i).y + GameLoop.particles.get(i).diameter);
                sb.draw(R.drawable.particle, src, dest, 0, Color.argb(GameLoop.particles.get(i).opacity, Color.red(GameLoop.particles.get(i).color), Color.green(GameLoop.particles.get(i).color), Color.blue(GameLoop.particles.get(i).color)));
            }
        }
        //draw player
//        paint.setColor(WorkoutTimer.characters.get(0).color);
//        paint.setAlpha(255);
//        sb.drawCircle(WorkoutTimer.characters.get(0).x,WorkoutTimer.characters.get(0).y,WorkoutTimer.characters.get(0).diameter/2,paint);
        src.set(0,0,64,64);
        dest.set(GameLoop.characters.get(0).x, GameLoop.characters.get(0).y, GameLoop.characters.get(0).x + GameLoop.characters.get(0).diameter, GameLoop.characters.get(0).y + GameLoop.characters.get(0).diameter);
        sb.draw(R.drawable.player, src, dest);

        //draw zombies
        for(int i = 1;i < GameLoop.characters.size(); i++){
//            paint.setColor(WorkoutTimer.characters.get(i).color);
//            paint.setStrokeWidth(3);
//            sb.drawCircle(WorkoutTimer.characters.get(i).x,WorkoutTimer.characters.get(i).y,WorkoutTimer.characters.get(i).diameter/2,paint);
            src.set(0, 0, 64, 64);
            dest.set(GameLoop.characters.get(i).x, GameLoop.characters.get(i).y, GameLoop.characters.get(i).x + GameLoop.characters.get(i).diameter, GameLoop.characters.get(i).y + GameLoop.characters.get(i).diameter);
            sb.draw(R.drawable.enemy, src, dest);
        }
        //draw bullets
        for(int i = 0;i < GameLoop.bullets.size(); i++){
//            paint.setColor(WorkoutTimer.bullets.get(i).color);
//            paint.setStrokeWidth(3);
//            sb.drawCircle(WorkoutTimer.bullets.get(i).x,WorkoutTimer.bullets.get(i).y,WorkoutTimer.bullets.get(i).diameter/2,paint);
            src.set(0, 0, 64, 64);
            dest.set(GameLoop.bullets.get(i).x, GameLoop.bullets.get(i).y, GameLoop.bullets.get(i).x + GameLoop.bullets.get(i).diameter, GameLoop.bullets.get(i).y + GameLoop.bullets.get(i).diameter);
            sb.draw(R.drawable.particle, src, dest);
        }
        //draw laser
        for(int i = 0;i < GameLoop.characters.get(0).collectedPowerups.size();i++){
            if(GameLoop.characters.get(0).collectedPowerups.get(i) == GameLoop.LASER){
//                paint.setColor(WorkoutTimer.laser.color);
//                paint.setStrokeWidth(WorkoutTimer.laser.width);
                sb.drawLine(R.drawable.enemy,src, GameLoop.laser.x1, GameLoop.laser.y1, GameLoop.laser.x2, GameLoop.laser.y2, GameLoop.laser.width);
            }
        }
        //draw powerups
        for(int i = 0;i < GameLoop.powerups.size();i++){
//            sb.drawBitmap(WorkoutTimer.powerups.get(i).sprite, WorkoutTimer.powerups.get(i).x - WorkoutTimer.powerups.get(i).diameter / 2,WorkoutTimer.powerups.get(i).y - WorkoutTimer.powerups.get(i).diameter / 2,paint);
            src.set(0,0,20,20);
            dest.set(GameLoop.powerups.get(i).x, GameLoop.powerups.get(i).y, GameLoop.powerups.get(i).x + GameLoop.powerups.get(i).diameter, GameLoop.powerups.get(i).y + GameLoop.powerups.get(i).diameter);
            sb.draw(GameLoop.powerups.get(i).sprite,src,dest);
        }
        //draw shield
        for(int i = 0;i < GameLoop.characters.get(0).collectedPowerups.size();i++){
            if(GameLoop.characters.get(0).collectedPowerups.get(i) == GameLoop.SHIELD){
                if(!GameLoop.gamePaused && !GameLoop.gameOver){
                    GameLoop.shieldAlive += 1;
                }
//                paint.setColor(Color.argb(70, 18, 255, 0));
//                paint.setStrokeWidth(3);
//                sb.drawCircle(WorkoutTimer.characters.get(0).x,WorkoutTimer.characters.get(0).y,WorkoutTimer.characters.get(0).diameter,paint);
//                paint.setColor(Color.argb(100,18,255,0));

//                sb.drawArc(new RectF(WorkoutTimer.characters.get(0).x - WorkoutTimer.characters.get(0).diameter / 2 - 13,WorkoutTimer.characters.get(0).y - WorkoutTimer.characters.get(0).diameter / 2 - 13,50,50),0,360 - 360 * WorkoutTimer.shieldAlive / WorkoutTimer.shieldLife,true,paint);
                if(GameLoop.shieldAlive > GameLoop.shieldLife){
                    GameLoop.characters.get(0).collectedPowerups.remove(i);
                    GameLoop.shieldAlive = 0;
                }
            }
        }

        //draw score
//        paint.setColor(Color.GRAY);
//        paint.setTextSize(30);
        sb.drawText(R.string.audiowide_font, GameLoop.totalScore + "",getWidth() - 200,getHeight() - 120,1.0f);

        if(!GameLoop.gamePaused && !GameLoop.gameOver){
            GameLoop.shootBullet();
        }
        else if(!GameLoop.gamePaused){
//            paint.setColor(Color.WHITE);
//            paint.setStrokeWidth(5);
//            paint.setTextSize(40);
            sb.drawText(R.string.audiowide_font,"Game Paused",100,150,1.0f);
//            paint.setTextSize(20);
            sb.drawText(R.string.audiowide_font,"Version: " + GameLoop.VERSION,100,190,1.0f);
        }
        else{
            //draw game over message if game over
//            paint.setColor(Color.RED);

//            paint.setStrokeWidth(5);
//            paint.setTextSize(40);
            sb.drawText(R.string.audiowide_font,"Game Over",100,150,1.0f,0xFFFF0000);
//            paint.setTextSize(20);
            sb.drawText(R.string.audiowide_font,"Version: " + GameLoop.VERSION,100,190,0.7f,0xFFFF0000);
//            paint.setColor(Color.argb(200,200,0,0));
        }
    }
}
