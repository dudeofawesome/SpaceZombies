package dudeofawesome.spaceZombies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;
import com.google.android.gms.ads.*;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.util.ArrayList;
import java.util.LinkedList;

import dudeofawesome.spaceZombies.util.IabException;
import dudeofawesome.spaceZombies.util.IabHelper;
import dudeofawesome.spaceZombies.util.IabResult;
import dudeofawesome.spaceZombies.util.Inventory;
import dudeofawesome.spaceZombies.util.Purchase;


public class Main extends BaseGameActivity implements OnClickListener, SensorEventListener {
    //constants
    public static final String VERSION = "1.41";

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
    private final static int MARGINFORERROR = 10;
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
    private static int zombiesKilled = 0;
    public static Context context;

    private static float[] accelerometerData = {0f,0f};
    private static float[] accelerometerZeroes = {0f,0f};
    private static boolean setAccelZeroes = false;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

//    private SoundPool soundPool;
//    private int ENEMYEXPLODESOUND;
//    private int YOUEXPLODESOUND;

    private boolean iveBeenSupported = false;

    private Handler frame = new Handler();


    private static final String TAG =
            "dudeofawesome.spacezombies";
    IabHelper mHelper;
    static final String ITEM_SKU = "dudeofawesome.spacezombies.removeads";

    Painter painter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);

        painter = (Painter)findViewById(R.id.gameCanvas);
//        painter = new Painter(this);
//        setContentView(painter);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        context = this.getBaseContext();

        SharedPreferences mPrefs = getSharedPreferences(TAG, 0);
        zombiesKilled = mPrefs.getInt("zombiesKilled", 0);

        characters.add(new character(screenWidth / 2,screenHeight / 2,15,7,Color.CYAN));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

//        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
//        ENEMYEXPLODESOUND = soundPool.load(context, R.raw.enemyexplode, 1);
//        YOUEXPLODESOUND = soundPool.load(context, R.raw.youexplode, 1);

        ((Button)findViewById(R.id.btnStart)).setOnClickListener(this);
        ((Button)findViewById(R.id.btnCalibrate)).setOnClickListener(this);
        ((Button)findViewById(R.id.btnAchievements)).setOnClickListener(this);
        ((Button)findViewById(R.id.btnLeaderboards)).setOnClickListener(this);
        ((Button)findViewById(R.id.btnBuy)).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        String base64EncodedPublicKey0 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxyDfpUx0t6ffrQAUkfEYGiM4b";
        String base64EncodedPublicKey1 = "fRu043AW5xDpVBQbnt10jRgD0NmdsVi+Zyua8yx9m1klesxA38eCV/GScZ+S2xBFoyS1SkSnTeT5PBPnLs1/eIOABIvlKdwQAk7BgCxERFcp2Q9CEr60j3V+VoD0tp0NVj9qE39rZ8KhnM5ZXwxhaAB6aHRQoyXwuMMfMXcmlhJWo";
        String base64EncodedPublicKey2 = "4xiPoeb8eNe7KqVQr0Fh4vSSuLdWMEcHV3RtLiUszucncQMTZ5NKCbALPepyAEsf/0T7AxYd0yYpONNX1zvJEKh1QX6VjmFwXLr9k4E+";
        String base64EncodedPublicKey3 = "r7uynJ5v9AyUbvMTd9SxVL6P7xuaCoJnEW1o8+gwIDAQAB";
        String base64EncodedPublicKey4 = "r7uynJ5v9AyUbvNTd9SxVL6P7xuaCoJnEW1o8+gwIDAQAB";

        if (base64EncodedPublicKey3 == "") {}

        mHelper = new IabHelper(this, base64EncodedPublicKey0 + base64EncodedPublicKey1 + base64EncodedPublicKey2 + base64EncodedPublicKey4);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);

                    if (iveBeenSupported)
                        findViewById(R.id.btnBuy).setVisibility(View.INVISIBLE);
                    else {
                        AdView adView = (AdView) findViewById(R.id.adView);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        adView.loadAd(adRequest);
                    }
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");

                    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
                        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                            Log.d(TAG, "Query inventory started"); //Log that were checking inventory
                            if (mHelper == null) return; // Have we been disposed of in the meantime? If so, quit
                            if (result.isFailure()) { // Is inventory query a failure?
                                Log.d(TAG, "Failed to query inventory: " + result);
                                return;
                            }
                            Log.d(TAG, "Query inventory was successful."); //if query not a failure then log success
                            Purchase premiumPurchase = inventory.getPurchase(ITEM_SKU); // Do we already have the premium upgrade?
                            iveBeenSupported = (premiumPurchase != null);//) && verifyDeveloperPayload(premiumPurchase));
                            Log.d(TAG, "User is " + (iveBeenSupported ? "PREMIUM" : "NOT PREMIUM")); //log if premium or not

                            if (iveBeenSupported)
                                findViewById(R.id.btnBuy).setVisibility(View.INVISIBLE);
                            else {
                                AdView adView = (AdView) findViewById(R.id.adView);
                                AdRequest adRequest = new AdRequest.Builder().build();
                                adView.loadAd(adRequest);
                            }
                        }
                    };

                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            }
        });

        if (isSignedIn()) {
            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        }

        startGame();
    }
    @Override
    public void onStart(){
        super.onStart();
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        gamePaused = true;
        showInterface();
    }

    private void showInterface() {
        findViewById(R.id.uiLayout).setVisibility(View.VISIBLE);
    }

    private void hideInterface() {
        findViewById(R.id.uiLayout).setVisibility(View.INVISIBLE);
    }

    synchronized public void startGame() {

//        ((Button)findViewById(R.id.btnStart)).setEnabled(true);
        //It's a good idea to remove any existing callbacks to keep
        //them from inadvertently stacking up.

        hideInterface();

        characters.clear();
        bullets.clear();
        particles.clear();
        powerups.clear();

        characters.clear();
        characters.add(new character(screenWidth / 2,screenHeight / 2,30,7,Color.CYAN));
        bullets.clear();
        particles.clear();
        powerups.clear();
        gameOver = false;
        gamePaused = false;
        totalScore = 0;
        addNewCharacterCounter = FREQUENCYOFNEWCHARS;
        smokeCounter = 0;
        twoshotLife = 200;
        twoshotAlive = 0;
        shotgunLife = 200;
        shotgunAlive = 0;
        shieldLife = 400;
        shieldAlive = 0;

        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    synchronized public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            if (gameOver)
                startGame();
            else if (gamePaused) {
                gamePaused = false;
                hideInterface();
            }
        }
        else if (v.getId() == R.id.btnCalibrate) {
            setAccelZeroes = true;
        }
        else if (v.getId() == R.id.sign_in_button) {
            // start the asynchronous sign in flow
            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            beginUserInitiatedSignIn();
        }
        else if (v.getId() == R.id.sign_out_button) {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
            signOut();
        }
        else if (v.getId() == R.id.btnAchievements) {
            if (isSignedIn())
                startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 5001);
            else
                showAlert("You need to sign in.");
        }
        else if (v.getId() == R.id.btnLeaderboards) {
            if (isSignedIn())
                startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), 5001);
//                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), String.valueOf(R.string.leaderboard_High_Scores)), 5001);
            else
                showAlert("You need to sign in.");
        }
        else if (v.getId() == R.id.btnBuy) {
            IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                    = new IabHelper.OnIabPurchaseFinishedListener() {
                public void onIabPurchaseFinished(IabResult result,
                                                  Purchase purchase)
                {
                    if (result.isFailure()) {
                        // Handle error
                        showAlert("The transaction seems to have failed, but you can remove ads anyways :)");
                        AdView adView = (AdView)findViewById(R.id.adView);
                        adView.destroy();
                        return;
                    }
                    else if (purchase.getSku().equals(ITEM_SKU)) {
                        iveBeenSupported = true;
                        AdView adView = (AdView)findViewById(R.id.adView);
                        adView.destroy();
                    }

                }
            };

            mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                    mPurchaseFinishedListener, "dudeofawesome.spacezombies.removeads");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
            if(circleCircleCollision(characters.get(0).x,characters.get(0).y,characters.get(0).diameter,characters.get(i).x,characters.get(i).y,characters.get(i).diameter,-1)){
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
//                    soundPool.play(ENEMYEXPLODESOUND, 1, 1, 0, 0, (float) Math.random() * 1.5f + 0.5f);
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

                        showInterface();

                        numOfParts = (int) (Math.random() * 50 + 300);
                        for(int k = 0; k < numOfParts;k++){
                            particles.add(new particle(EXPLOSION,characters.get(0).x,characters.get(0).y,10));
                        }

                        characters.remove(0);

//                        soundPool.play(YOUEXPLODESOUND, 1, 1, 0, 0, 1);

                        awardAchievements();
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
//                    soundPool.play(ENEMYEXPLODESOUND, 1, 1, 0, 0, (float) Math.random() * 1.5f + 0.5f);

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

                    int randomNumber = (int) (Math.random() * 200);
                    if(randomNumber == 3){
                        //add new powerup
                        switch((int) (Math.random() * 11)){
                            case 0: case 1:
                                powerups.add(new powerup(LASER,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 2:
                                powerups.add(new powerup(NUKE,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 3: case 4: case 5: case 6:
                                powerups.add(new powerup(TWOSHOT,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 7: case 8: case 9:
                                powerups.add(new powerup(SHOTGUN,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 10:
                                powerups.add(new powerup(HEALTH,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                            case 11:
                                powerups.add(new powerup(SHIELD,characters.get(j).x + characters.get(j).diameter / 2,characters.get(j).y + characters.get(j).diameter / 2));
                                break;
                        }
                    }

                    //remove bullet and character
                    characters.remove(j);
                    bullets.remove(i);

//                    soundPool.play(ENEMYEXPLODESOUND, 1, 1, 0, 0, (float) Math.random() * 1.5f + 0.5f);

                    increase = false;

                    //give 100 points for killing enemy
                    totalScore += 100;
                    zombiesKilled++;

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

//                        soundPool.play(ENEMYEXPLODESOUND, 1, 1, 0, 0, (float) Math.random() * 1.5f + 0.5f);

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

    private void awardAchievements() {
        if (isSignedIn()) {
            if (zombiesKilled >= 1)
                Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_Zombie_Slayer));
            if (zombiesKilled >= 50)
                Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_Better_Zombie_Slayer));
            if (zombiesKilled >= 500)
                Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_Super_Zombie_Slayer));
            if (zombiesKilled >= 5000)
                Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_Ultimate_Zombie_Slayer));
            if (zombiesKilled >= 500000)
                Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_Supreme_Overlord_Zombie_Slayer));
            if (totalScore > 5000)
                Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_High_Scorer));
            if (totalScore > 20000)
                Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_Higher_Scorer));

            Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_High_Scores), totalScore);
        }

        SharedPreferences mPrefs = getSharedPreferences(TAG, 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("zombiesKilled", zombiesKilled).commit();
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
            bullets.add(new bullet(characters.get(0).x /*+ characters.get(0).diameter / 2*/,characters.get(0).y /*+ characters.get(0).diameter / 2*/,2,_vX,_vY,Color.WHITE));

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

        int _diameter = (int) (Math.random() * 5 + 15);
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
            }
            painter.paint();



            frame.removeCallbacks(frameUpdate);
            //make any updates to on screen objects here
            //then invoke the on draw by invalidating the canvas
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelerometerData[0] = event.values[0] - accelerometerZeroes[0];
        accelerometerData[1] = event.values[1] - accelerometerZeroes[1];
        if (setAccelZeroes) {
            accelerometerZeroes[0] = event.values[0];
            accelerometerZeroes[1] = event.values[1];
            setAccelZeroes = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {
        if (!gamePaused) {
            gamePaused = true;
            showInterface();
        }
        else if (gamePaused) {
            gamePaused = false;
            hideInterface();
        }
        else if (gameOver) {
            startGame();
            hideInterface();
        }
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {
        findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
    }
}
