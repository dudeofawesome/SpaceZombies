package dudeofawesome.spaceZombies;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class DrawWorkoutTimer extends View {
    private Paint paint;

    public DrawWorkoutTimer(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawWorkoutTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawWorkoutTimer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Set up a default Paint object
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);

        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


         //draw particles
         for(int i = 0; i < WorkoutTimer.particles.size(); i++){
             paint.setColor(WorkoutTimer.particles.get(i).color);
             paint.setAlpha(WorkoutTimer.particles.get(i).opacity);
             paint.setStrokeWidth(WorkoutTimer.particles.get(i).lineWidth);
             canvas.drawCircle(WorkoutTimer.particles.get(i).x,WorkoutTimer.particles.get(i).y,WorkoutTimer.particles.get(i).diameter/2,paint);
         }
         //draw player
         paint.setColor(WorkoutTimer.characters.get(0).color);
         paint.setAlpha(255);
         canvas.drawCircle(WorkoutTimer.characters.get(0).x,WorkoutTimer.characters.get(0).y,WorkoutTimer.characters.get(0).diameter/2,paint);

         //draw zombies
         for(int i = 1;i < WorkoutTimer.characters.size(); i++){
             paint.setColor(WorkoutTimer.characters.get(i).color);
             paint.setStrokeWidth(3);
             canvas.drawCircle(WorkoutTimer.characters.get(i).x,WorkoutTimer.characters.get(i).y,WorkoutTimer.characters.get(i).diameter/2,paint);
         }
         //draw bullets
         for(int i = 0;i < WorkoutTimer.bullets.size(); i++){
             paint.setColor(WorkoutTimer.bullets.get(i).color);
             paint.setStrokeWidth(3);
             canvas.drawCircle(WorkoutTimer.bullets.get(i).x,WorkoutTimer.bullets.get(i).y,WorkoutTimer.bullets.get(i).diameter/2,paint);
         }
         //draw laser
         for(int i = 0;i < WorkoutTimer.characters.get(0).collectedPowerups.size();i++){
             if(WorkoutTimer.characters.get(0).collectedPowerups.get(i) == WorkoutTimer.LASER){
                 paint.setColor(WorkoutTimer.laser.color);
                 paint.setStrokeWidth(WorkoutTimer.laser.width);
                 canvas.drawLine(WorkoutTimer.laser.x1,WorkoutTimer.laser.y1,WorkoutTimer.laser.x2,WorkoutTimer.laser.y2,paint);
             }
         }
         //draw powerups
         for(int i = 0;i < WorkoutTimer.powerups.size();i++){
             canvas.drawBitmap(WorkoutTimer.powerups.get(i).sprite, WorkoutTimer.powerups.get(i).x - WorkoutTimer.powerups.get(i).diameter / 2,WorkoutTimer.powerups.get(i).y - WorkoutTimer.powerups.get(i).diameter / 2,paint);
         }
         //draw shield
         for(int i = 0;i < WorkoutTimer.characters.get(0).collectedPowerups.size();i++){
             if(WorkoutTimer.characters.get(0).collectedPowerups.get(i) == WorkoutTimer.SHIELD){
                 if(!WorkoutTimer.gamePaused && !WorkoutTimer.gameOver){
                     WorkoutTimer.shieldAlive += 1;
                 }
                 paint.setColor(Color.argb(70,18,255,0));
                 paint.setStrokeWidth(3);
                 canvas.drawCircle(WorkoutTimer.characters.get(0).x,WorkoutTimer.characters.get(0).y,WorkoutTimer.characters.get(0).diameter,paint);
                 paint.setColor(Color.argb(100,18,255,0));
                 canvas.drawArc(new RectF(WorkoutTimer.characters.get(0).x - WorkoutTimer.characters.get(0).diameter / 2 - 13,WorkoutTimer.characters.get(0).y - WorkoutTimer.characters.get(0).diameter / 2 - 13,50,50),0,360 - 360 * WorkoutTimer.shieldAlive / WorkoutTimer.shieldLife,true,paint);
                 if(WorkoutTimer.shieldAlive > WorkoutTimer.shieldLife){
                     WorkoutTimer.characters.get(0).collectedPowerups.remove(i);
                     WorkoutTimer.shieldAlive = 0;
                 }
             }
         }

         //draw score
         paint.setColor(Color.GRAY);
         paint.setTextSize(30);
         canvas.drawText(WorkoutTimer.totalScore + "",getWidth() - 200,getHeight() - 120,paint);

         if(!WorkoutTimer.gamePaused && !WorkoutTimer.gameOver){
             WorkoutTimer.shootBullet();
         }
         else if(!WorkoutTimer.gamePaused){
             paint.setColor(Color.WHITE);
             paint.setStrokeWidth(5);
             paint.setTextSize(40);
             canvas.drawText("Game Paused",80,150,paint);
             paint.setTextSize(20);
             canvas.drawText("Version: " + WorkoutTimer.VERSION,80,190,paint);
         }
         else{
             //draw game over message if game over
             paint.setColor(Color.RED);
             paint.setStrokeWidth(5);
             paint.setTextSize(40);
             canvas.drawText("Game Over",80,150,paint);
             paint.setTextSize(20);
             canvas.drawText("Version: " + WorkoutTimer.VERSION,80,190,paint);
             paint.setColor(Color.argb(200,200,0,0));
         }
    }
}
