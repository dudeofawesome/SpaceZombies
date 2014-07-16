package dudeofawesome.spaceZombies;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class Painter extends SurfaceView implements SurfaceHolder.Callback {
    private Paint paint;
    private SurfaceHolder holder;

    public Painter(Context context) {
        super(context);
        init(null, 0);

        holder = getHolder();
    }

    public Painter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

        holder = getHolder();
    }

    public Painter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);

        holder = getHolder();
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Set up a default Paint object
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);

        
    }

    public void paint() {
        Canvas canvas = holder.lockCanvas();
        System.out.println("frame");

        if (canvas != null) {
            //clear canvas
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            //draw particles
            for (int i = 0; i < Main.particles.size(); i++) {
                paint.setColor(Main.particles.get(i).color);
                paint.setAlpha(Main.particles.get(i).opacity);
                paint.setStrokeWidth(Main.particles.get(i).lineWidth);
                canvas.drawCircle(Main.particles.get(i).x, Main.particles.get(i).y, Main.particles.get(i).diameter / 2, paint);
            }
            //draw player
            paint.setColor(Main.characters.get(0).color);
            paint.setAlpha(255);
            canvas.drawCircle(Main.characters.get(0).x, Main.characters.get(0).y, Main.characters.get(0).diameter / 2, paint);

            //draw zombies
            for (int i = 1; i < Main.characters.size(); i++) {
                paint.setColor(Main.characters.get(i).color);
                paint.setStrokeWidth(3);
                canvas.drawCircle(Main.characters.get(i).x, Main.characters.get(i).y, Main.characters.get(i).diameter / 2, paint);
            }
            //draw bullets
            for (int i = 0; i < Main.bullets.size(); i++) {
                paint.setColor(Main.bullets.get(i).color);
                paint.setStrokeWidth(3);
                canvas.drawCircle(Main.bullets.get(i).x, Main.bullets.get(i).y, Main.bullets.get(i).diameter / 2, paint);
            }
            //draw laser
            for (int i = 0; i < Main.characters.get(0).collectedPowerups.size(); i++) {
                if (Main.characters.get(0).collectedPowerups.get(i) == Main.LASER) {
                    paint.setColor(Main.laser.color);
                    paint.setStrokeWidth(Main.laser.width);
                    canvas.drawLine(Main.laser.x1, Main.laser.y1, Main.laser.x2, Main.laser.y2, paint);
                }
            }
            //draw powerups
            for (int i = 0; i < Main.powerups.size(); i++) {
                canvas.drawBitmap(Main.powerups.get(i).sprite, Main.powerups.get(i).x - Main.powerups.get(i).diameter / 2, Main.powerups.get(i).y - Main.powerups.get(i).diameter / 2, paint);
            }
            //draw shield
            for (int i = 0; i < Main.characters.get(0).collectedPowerups.size(); i++) {
                if (Main.characters.get(0).collectedPowerups.get(i) == Main.SHIELD) {
                    if (!Main.gamePaused && !Main.gameOver) {
                        Main.shieldAlive += 1;
                    }
                    paint.setColor(Color.argb(70, 18, 255, 0));
                    paint.setStrokeWidth(3);
                    canvas.drawCircle(Main.characters.get(0).x, Main.characters.get(0).y, Main.characters.get(0).diameter, paint);
                    paint.setColor(Color.argb(100, 18, 255, 0));
                    canvas.drawArc(new RectF(Main.characters.get(0).x - Main.characters.get(0).diameter / 2 - 13, Main.characters.get(0).y - Main.characters.get(0).diameter / 2 - 13, 50, 50), 0, 360 - 360 * Main.shieldAlive / Main.shieldLife, true, paint);
                    if (Main.shieldAlive > Main.shieldLife) {
                        Main.characters.get(0).collectedPowerups.remove(i);
                        Main.shieldAlive = 0;
                    }
                }
            }

            //draw score
            paint.setColor(Color.GRAY);
            paint.setTextSize(30);
            canvas.drawText(Main.totalScore + "", getWidth() - 200, getHeight() - 120, paint);

            if (!Main.gamePaused && !Main.gameOver) {
                Main.shootBullet();
            } else if (!Main.gamePaused) {
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(5);
                paint.setTextSize(40);
                canvas.drawText("Game Paused", 80, 150, paint);
                paint.setTextSize(20);
                canvas.drawText("Version: " + Main.VERSION, 80, 190, paint);
            } else {
                //draw game over message if game over
                paint.setColor(Color.RED);
                paint.setStrokeWidth(5);
                paint.setTextSize(40);
                canvas.drawText("Game Over", 80, 150, paint);
                paint.setTextSize(20);
                canvas.drawText("Version: " + Main.VERSION, 80, 190, paint);
                paint.setColor(Color.argb(200, 200, 0, 0));
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
