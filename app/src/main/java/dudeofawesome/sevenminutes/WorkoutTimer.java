package dudeofawesome.sevenminutes;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;

import java.util.LinkedList;

public class WorkoutTimer extends Activity implements OnClickListener, SensorEventListener {
    //constants
    public static final double VERSION = 0.44;

    public static final int EXPLOSION = 0;
    public static final int SMOKE = 1;
    public static final int FIRE = 2;
    public static final int EXHAUST = 3;
    public static final int POWERUPDUST = 4;

    public static final int LASER = 0;
    public static final int HEALTH = 1;
    public static final int NUKE = 2;
    public static final int TWOSHOT = 3;
    public static final int SHOTGUN = 4;
    public static final int SHIELD = 5;

    private static int FREQUENCYOFNEWCHARS = 30;
    private static final int FRAME_RATE = 1000/30;
    private final static int MARGINFORERROR = 3;
    public static int screenWidth, screenHeight;

    public static LinkedList<character> characters = new LinkedList<character>();
    public static LinkedList<bullet> bullets = new LinkedList<bullet>();
    public static LinkedList<particle> particles = new LinkedList<particle>();
    public static LinkedList<powerup> powerups = new LinkedList<powerup>();
    public static boolean gameOver = true;
    public static boolean gamePaused = false;//true;
    public static int totalScore = 0;
    private static int addNewCharacterCounter = FREQUENCYOFNEWCHARS;
    private static int smokeCounter = 0;
    public static Laser laser = new Laser(-10,-10,-11,-11);
    private static int twoshotLife = 200;
    private static int twoshotAlive = 0;
    private static int shotgunLife = 200;
    private static int shotgunAlive = 0;
    public static int shieldLife = 400;
    public static int shieldAlive = 0;

    private static float[] accelerometerData;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;



    private DrawWorkoutTimer drawWorkoutTimer;
    private Handler frame = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        characters.add(new character(350,350,15,7,Color.CYAN));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        ((Button)findViewById(R.id.btnStart)).setOnClickListener(this);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    synchronized public void startGame() {
        ((Button)findViewById(R.id.btnStart)).setEnabled(true);
        //It's a good idea to remove any existing callbacks to keep
        //them from inadvertently stacking up.

        findViewById(R.id.btnStart).setVisibility(View.GONE);

        characters.clear();
        bullets.clear();
        particles.clear();
        powerups.clear();

        characters.add(new character(350,350,15,7,Color.CYAN));

        gameOver = false;
        gamePaused = false;

        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    synchronized public void onClick(View v) {
        startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.workout_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






    public void movePlayer(){
        //move your dot based on its assigned velocity
        // characters.get(0).move(MOUSEXDISPLACEMENT,MOUSEYDISPLACEMENT);
        int _mouseX = characters.get(0).x + (int) (accelerometerData[0] * -1000);
        int _mouseY = characters.get(0).y + (int) (accelerometerData[1] * 1000);
        characters.get(0).move(_mouseX,_mouseY);

        if(smokeCounter >= 2){

            particles.add(new particle(EXHAUST,characters.get(0).x,characters.get(0).y,1));

            if(characters.get(0).health <= 75){
                particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
            }
            if(characters.get(0).health <= 50){
                particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
                particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
            }
            if(characters.get(0).health <= 25){
                particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
                particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
                particles.add(new particle(SMOKE,characters.get(0).x,characters.get(0).y));
                particles.add(new particle(FIRE,characters.get(0).x,characters.get(0).y));
                particles.add(new particle(FIRE,characters.get(0).x,characters.get(0).y));
            }
            smokeCounter = 0;
        }
        else{
            smokeCounter ++;
        }
    }

    public void moveEnemies(){
        //move the other entities towards your position
        for(int i = 1;i < characters.size();i++){
            characters.get(i).move(characters.get(0).x,characters.get(0).y);
            for(int j = 1;j < characters.size();j++){
                if(i != j && circleCircleCollision(characters.get(i).x,characters.get(i).y,characters.get(i).diameter,characters.get(j).x,characters.get(j).y,characters.get(j).diameter,-3)){
                    characters.get(i).moveBack();
                }
            }
        }
    }

    public void moveBullets(){
        for(int i = 1;i < bullets.size();i++){
            //changes position
            bullets.get(i).move();
        }
    }

    public void moveParticles(){
        int i = 0;
        while(i < particles.size()){
            if(particles.get(i).move()){
                i++;
            }
            else{
                particles.remove(i);
            }
        }
    }

    public void movePowerups(){
        int i = 0;
        while(i < powerups.size()){
            if(Math.floor(Math.random() * 5) == 2){
                particles.add(new particle(POWERUPDUST,powerups.get(i).x,powerups.get(i).y));
            }
            if(powerups.get(i).move()){
                i++;
            }
            else{
                powerups.remove(i);
            }
        }
    }

    public void testCollision(){
        //test for enemies touching you
        for(int i = 1;i < characters.size();i++){
            if(circleCircleCollision(characters.get(0).x,characters.get(0).y,characters.get(0).diameter,characters.get(i).x,characters.get(i).y,characters.get(i).diameter,0)){
                boolean shieldActive = false;
                for(int j = 0;j < characters.get(0).collectedPowerups.size();j++){
                    if(characters.get(0).collectedPowerups.get(j) == SHIELD){
                        shieldActive = true;
                    }
                }
                if(shieldActive){
                    int numOfParts = (int) (Math.random() * 10 + 10);
                    for(int k = 0; k < numOfParts;k++){
                        particles.add(new particle(EXPLOSION,characters.get(i).x,characters.get(i).y));
                    }
                    characters.remove(i);
                    //@debug
                    //new AePlayWave(getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();
                }
                else{
                    characters.get(0).health -= 25;
                    int numOfParts = (int) (Math.random() * 10 + 10);
                    for(int k = 0; k < numOfParts;k++){
                        particles.add(new particle(EXPLOSION,characters.get(i).x,characters.get(i).y));
                    }
                    characters.remove(i);
                    if(characters.get(0).health <= 0){
                        //game over
                        gameOver = true;

                        findViewById(R.id.btnStart).setVisibility(View.VISIBLE);

                        numOfParts = (int) (Math.random() * 50 + 300);
                        for(int k = 0; k < numOfParts;k++){
                            particles.add(new particle(EXPLOSION,characters.get(0).x,characters.get(0).y,10));
                        }

                        characters.remove(0);

                        //@debug
                        //new AePlayWave(getClass().getClassLoader().getResourceAsStream("/sounds/youExplode.wav").toString().split(":")[1]).start();
                    }
                }
            }
        }

        //test for you collecting powerups
        int i = 0;
        while(i < powerups.size()){
            if(circleCircleCollision(characters.get(0).x,characters.get(0).y,characters.get(0).diameter,powerups.get(i).x,powerups.get(i).y,powerups.get(i).diameter,MARGINFORERROR)){
                if(powerups.get(i).type == HEALTH){
                    characters.get(0).health += 25;
                }
                else if(powerups.get(i).type == NUKE){
                    totalScore += characters.size() * 100;
                    character player = characters.get(0);
                    characters.clear();
                    //@debug
                    //new AePlayWave(getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();

                    characters.add(player);
                    int numOfParts = (int) (Math.random() * 20 + 100);
                    for(int k = 0; k < numOfParts;k++){
                        particles.add(new particle(EXPLOSION,characters.get(0).x,characters.get(0).y,50));
                    }
                }
                else{
                    int numberOfPowerups = characters.get(0).collectedPowerups.size();
                    boolean powerupNotTaken = true;
                    for(int j = 0;j < characters.get(0).collectedPowerups.size();j++){
                        if(characters.get(0).collectedPowerups.get(j) == powerups.get(i).type){
                            powerupNotTaken = false;
                        }
                    }
                    if(powerupNotTaken){
                        characters.get(0).collectedPowerups.add(powerups.get(i).type);
                    }
                    else{
                        switch(powerups.get(i).type){
                            case SHIELD:
                                shieldAlive = 0;
                                break;
                            case SHOTGUN:
                                shotgunAlive = 0;
                                break;
                            case TWOSHOT:
                                twoshotAlive = 0;
                                break;
                            case LASER:
                                laser.alive = 0;
                                break;
                        }
                    }
                }

                int numOfParts = (int) (Math.random() * 10 + 35);
                for(int j = 0; j < numOfParts;j++){
                    particles.add(new particle(EXPLOSION,powerups.get(i).x,powerups.get(i).y,10));
                }

                powerups.remove(i);

                totalScore += 300;
            }
            else{
                i++;
            }
        }

        //test for bullets touching enemies
        i = 0;
        while(i < bullets.size()){// - 1){
            boolean increase = true;
            int j = 1;
            while(j < characters.size()){
                if(j < characters.size() && i < bullets.size() && circleCircleCollision(bullets.get(i).x,bullets.get(i).y,bullets.get(i).diameter,characters.get(j).x,characters.get(j).y,characters.get(j).diameter,MARGINFORERROR)){
                    //make particles
                    int numOfParts = (int) (Math.random() * 5 + 10);
                    for(int k = 0; k < numOfParts;k++){
                        particles.add(new particle(EXPLOSION,characters.get(j).x,characters.get(j).y));
                    }

                    int randomNumber = (int) (Math.random() * 35);
                    if(randomNumber == 3){
                        //@debug
                        // if(0 == 0){
                        //add new powerup
                        switch((int) (Math.random() * 7)){
                            case 0:
//                                powerups.add(new powerup(LASER,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 1:
//                                powerups.add(new powerup(NUKE,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 2:
//                                powerups.add(new powerup(TWOSHOT,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 3:
//                                powerups.add(new powerup(SHOTGUN,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 4: case 5:
//                                powerups.add(new powerup(HEALTH,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 6:
//                                powerups.add(new powerup(SHIELD,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                        }
                    }

                    //remove bullet and character
                    characters.remove(j);
                    bullets.remove(i);

                    //@debug
                    // System.out.println(getClass().getResource("sounds/enemyExplode.wav").toString().split(":")[0] + "..." + getClass().getResource("sounds/enemyExplode.wav").toString().split(":")[1] + "..." + getClass().getResource("sounds/enemyExplode.wav").toString().split(":")[2]);
//                    if(this.getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1].equals("file")){
                        //new AePlayWave(this.getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[2]).start();
//                    }
//                    else{
                        //new AePlayWave(this.getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();
//                    }

                    increase = false;

                    //give 100 points for killing enemy
                    totalScore += 100;

                    //add an enemy
                    if(laser.alive == 0 && twoshotAlive == 0 && shotgunAlive == 0){
                        addEnemy();
                    }
                }
                else{
                    //i++;
                    j++;
                }
            }
            if(increase == true){
                i++;
            }
        }

        //test for laser touching enemies
        for(i = 0;i < characters.get(0).collectedPowerups.size();i++){
            if(characters.get(0).collectedPowerups.get(i) == LASER){
                int j = 1;
                while(j < characters.size()){
                    if(circleLineCollision(characters.get(j).x,characters.get(j).y,characters.get(j).diameter,laser.x1,laser.y1,laser.x2,laser.y2,MARGINFORERROR)){
                        int numOfParts = (int) (Math.random() * 5 + 10);
                        for(int k = 0; k < numOfParts;k++){
                            particles.add(new particle(EXPLOSION,characters.get(j).x,characters.get(j).y));
                        }

                        //remove bullet and character
                        characters.remove(j);

                        //@debug
                        //new AePlayWave(getClass().getResource("/sounds/enemyExplode.wav").toString().split(":")[1]).start();

                        //give 150 points for killing enemy
                        totalScore += 150;
                    }
                    else{
                        j++;
                    }
                }
            }
        }

        //test to make sure bullet is still on screen
        for(i = 0;i < bullets.size();i++){
            if(bullets.get(i).x < 0 || bullets.get(i).x > screenWidth ||
                    bullets.get(i).y < 0 || bullets.get(i).y > screenHeight){
                //remove bullet
                bullets.remove(i);
            }
        }
    }

    public boolean circleCircleCollision(int _x1,int _y1,int _diam1,int _x2,int _y2,int _diam2,int _marginForError){
        double _distance = Math.sqrt(Math.pow((_x1 - _diam1 / 2) - (_x2 - _diam2 / 2),2) + Math.pow((_y1 - _diam1 / 2) - (_y2 - _diam2 / 2), 2));
        if(_distance <= (_diam1 / 2) + (_diam2 / 2) + _marginForError){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean circleLineCollision(int _cX,int _cY,int _diam,int _lX1,int _lY1,int _lX2,int _lY2,int _marginForError){
        //turn vectors into component form
        int _lX = _lX2 - _lX1;
        int _lY = _lY2 - _lY1;
        _cX = _cX - _lX1;
        _cY = _cY - _lY1;

        double _CdotL = _cX * _lX + _cY * _lY; //C dot L
        double _magC = Math.hypot(_cX,_cY); //magnitude of C
        double _magL = Math.hypot(_lX,_lY); //magnitude of L
        //make sure that the enemy is in front of player
        //A dot B = mag(A) * mag(B) * cos(Theda)
        //cos(Theda) is positive for angles <= PI/2  ie. enemy in front of player
        if(_CdotL / (_magC * _magL) >= 0){
            //get distance to shortest point on line
            //reference: http://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
            double _distance = Math.abs(_cX * _lY - _cY * _lX) / _magL;
            if(_distance <= (_diam / 2) + _marginForError){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public static void shootBullet(){
//        int _mouseX = MouseInfo.getPointerInfo().getLocation().x + MOUSEXDISPLACEMENT, _mouseY = MouseInfo.getPointerInfo().getLocation().y + MOUSEYDISPLACEMENT;
        int _mouseX = characters.get(0).x + (int) (accelerometerData[0] * -1000);
        int _mouseY = characters.get(0).y + (int) (accelerometerData[1] * 1000);
        boolean laserShooting = false;
        boolean twoshot = false;
        boolean shotgun = false;

        for(int i = 0;i < characters.get(0).collectedPowerups.size();i++){
            if(characters.get(0).collectedPowerups.get(i) == LASER){
                laserShooting = true;
            }
            else if(characters.get(0).collectedPowerups.get(i) == TWOSHOT){
                if(twoshotAlive < twoshotLife){
                    twoshotAlive += 1;
                    twoshot = true;
                }
                else{
                    characters.get(0).collectedPowerups.remove(i);
                    twoshotAlive = 0;
                }
            }
            else if(characters.get(0).collectedPowerups.get(i) == SHOTGUN){
                if(shotgunAlive < shotgunLife){
                    shotgunAlive += 1;
                    shotgun = true;
                }
                else{
                    characters.get(0).collectedPowerups.remove(i);
                    shotgunAlive = 0;
                }
            }
        }
        if(laserShooting){
            if(laser.alive <= laser.life){
                laser.move(characters.get(0).x + characters.get(0).diameter / 2 ,characters.get(0).y + characters.get(0).diameter / 2 ,_mouseX,_mouseY,screenWidth,screenHeight);
                laser.alive++;
            }
            else{
                for(int i = 0;i < characters.get(0).collectedPowerups.size();i++){
                    if(characters.get(0).collectedPowerups.get(i) == LASER){
                        characters.get(0).collectedPowerups.remove(i);
                        laser.alive = 0;
                    }
                }
            }
        }
        else{
            double _vX,_vY;
            double _angleTo;

            //remove 1 point for using ammo
            totalScore -= 1;

            //get angle to cursor
            _angleTo = Math.atan2((_mouseY - characters.get(0).y),(_mouseX - characters.get(0).x));

            //split it up into x and y
            _vX = Math.cos(_angleTo) * 15;
            _vY = Math.sin(_angleTo) * 15;
            bullets.add(new bullet(characters.get(0).x + characters.get(0).diameter / 2,characters.get(0).y + characters.get(0).diameter / 2,2,_vX,_vY,Color.WHITE));

            if(twoshot){
                for(int i = 0;i < 6;i++){

                    _angleTo += Math.PI;

                    //split it up into x and y
                    _vX = Math.cos(_angleTo) * 15;
                    _vY = Math.sin(_angleTo) * 15;
                    bullets.add(new bullet(characters.get(0).x + characters.get(0).diameter / 2,characters.get(0).y + characters.get(0).diameter / 2,2,_vX,_vY,Color.WHITE));
                }
            }
            if(shotgun){
                _angleTo -= 3 * Math.PI / 12;
                for(int i = 0;i < 6;i++){

                    _angleTo += Math.PI / 12;

                    //split it up into x and y
                    _vX = Math.cos(_angleTo) * 15;
                    _vY = Math.sin(_angleTo) * 15;
                    bullets.add(new bullet(characters.get(0).x + characters.get(0).diameter / 2,characters.get(0).y + characters.get(0).diameter / 2,2,_vX,_vY,Color.WHITE));
                }
            }
        }
    }

    public void addEnemy(){
        //find a place to put enemy
        int _circum = (screenWidth * 2) + (screenHeight * 2);
        int _placeOnEdge = (int) (Math.random() * _circum);
        int _xLoc = 0;
        int _yLoc = 0;
        if(_placeOnEdge > screenWidth){
            if(_placeOnEdge > screenWidth + screenHeight){
                if(_placeOnEdge > screenWidth + screenHeight + screenWidth){
                    _yLoc = screenHeight - (_placeOnEdge - screenWidth - screenHeight - screenWidth);
                }
                else{
                    _xLoc = screenWidth - (_placeOnEdge - screenWidth - screenHeight);
                    _yLoc = screenHeight;
                }
            }
            else{
                _yLoc = _placeOnEdge - screenWidth;
                _xLoc = screenWidth;
            }
        }
        else{
            _xLoc = _placeOnEdge;
        }

        int _diameter = (int) (Math.random() * 5 + 9);
        // int _totalV = (int) ((16 - _diameter) * 0.85);
        int _totalV = (int) ((24 - _diameter) / 3);
        // int _totalV = 3;
        //use constructor from character.java to add in the enemy
        characters.add(new character(_xLoc,_yLoc,_diameter,_totalV,Color.RED));
    }










    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            if(gamePaused != true){
                if(gameOver != true){
                    movePlayer();
                    moveEnemies();
                    testCollision();
                    addNewCharacterCounter -= 1;
                    if(addNewCharacterCounter <= 0){
                        addEnemy();
                        addNewCharacterCounter = FREQUENCYOFNEWCHARS;
                    }
                }
                moveBullets();
                moveParticles();
                movePowerups();
                ((DrawWorkoutTimer)findViewById(R.id.WorkoutTimerView)).invalidate();
            }
            else{
                ((DrawWorkoutTimer)findViewById(R.id.WorkoutTimerView)).invalidate();
            }


            frame.removeCallbacks(frameUpdate);
            //make any updates to on screen objects here
            //then invoke the on draw by invalidating the canvas
//            ((DrawWorkoutTimer)findViewById(R.id.WorkoutTimerView)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
//        System.out.println(event.values[0]+","+event.values[1]+","+event.values[2]);
        accelerometerData = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
