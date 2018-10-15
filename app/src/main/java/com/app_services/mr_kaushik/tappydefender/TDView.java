package com.app_services.mr_kaushik.tappydefender;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TDView extends SurfaceView implements Runnable {

    MediaPlayer start, win, destroyed, bump, race;

    private boolean gameEnded;


    private Context context;

    private float distance;
    private float highestDistance;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    volatile boolean playing;
    Thread gameThread = null;

    private  PlayerShip player;


    public EnemyShip enemyShip1;
    public EnemyShip enemyShip2;
    public EnemyShip enemyShip3;
    public EnemyShip enemyShip4;

    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    private int screenX;
    private int screenY;



    private SharedPreferences prefs, prefs_1;
    private SharedPreferences.Editor editor, editor_1;


    public TDView(Context context, int x, int y) {

        super(context);

        this.context = context;


       try{
            start = MediaPlayer.create(context, R.raw.start);
            destroyed = MediaPlayer.create(context, R.raw.exployed);
            win = MediaPlayer.create(context, R.raw.win);
            bump = MediaPlayer.create(context, R.raw.bump);
            race = MediaPlayer.create(context, R.raw.race);



        }catch(Exception e){
            Log.e("error", "failed to load sound files");
        }

        screenX = x;
        screenY = y;

        ourHolder = getHolder();
        paint = new Paint();




        prefs = context.getSharedPreferences("PreviousScore", context.MODE_PRIVATE);
        editor = prefs.edit();
        fastestTime = prefs.getLong("fastestTime", fastestTime);
        highestDistance = prefs.getFloat("highestDistance", highestDistance);
        startGame();

    }

    private void startGame(){

        start.start();

        player = new PlayerShip(context, screenX, screenY);

        enemyShip1 = new EnemyShip(context, screenX, screenY);
        enemyShip2 = new EnemyShip(context, screenX, screenY);
        enemyShip3 = new EnemyShip(context, screenX, screenY);

        if(screenX > 1000){
            enemyShip4 = new EnemyShip(context, screenX, screenY);
        }

        int num = 40;
        for ( int i=0; i < num; i++)
        {
            SpaceDust spacedust = new SpaceDust(screenX, screenY);
            dustList.add(spacedust);
        }
        distance = 0;
        timeTaken = 0;
        timeStarted = System.currentTimeMillis();

        gameEnded = false;
        start.start();
    }


    public void pause(){
        playing = false;
        try{
            gameThread.join();
        }catch (InterruptedException e){
            Log.d(TAG, "pause: Thread Pause : " + e);
        }
    }


    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(playing){
            update();
            draw();
            control();
        }
    }

    private String formatTime(long timeTaken){
        long seconds = (timeTaken) / 1000;
        long thousandthSecond = (timeTaken) - (seconds * 1000);
        String strThousandth = " " + thousandthSecond;
        if(thousandthSecond < 100){
            strThousandth = "0" + thousandthSecond;
        }
        if(thousandthSecond < 10){
            strThousandth = "0" + strThousandth;
        }
        String stringTime = " " + seconds + "." + strThousandth;
        return stringTime;

    }

    private void update(){

        boolean hitDetected = false;

        if(Rect.intersects(player.getHitBox(), enemyShip1.getHitBox())){
            hitDetected = true;
            enemyShip1.setX(-100);
        }


        if(Rect.intersects(player.getHitBox(), enemyShip2.getHitBox())){
            hitDetected = true;
            enemyShip2.setX(-100);
        }

        if(Rect.intersects(player.getHitBox(), enemyShip3.getHitBox())){
            hitDetected = true;
            enemyShip3.setX(-100);
        }
        if(screenX > 1000) {
            if (Rect.intersects(player.getHitBox(), enemyShip4.getHitBox())) {
                hitDetected = true;
                enemyShip4.setX(-100);
            }
        }

        if(hitDetected){
            bump.start();
            player.reduceStrength();
            if(player.getShieldStrength() < 0){
                gameEnded = true;
            }
        }

        player.update();
        if(distance % 10000 == 0){
            player.setSpeed(player.getSpeed() + 5);
            enemyShip1.setSpeed(enemyShip1.getSpeed() + 4);
            enemyShip2.setSpeed(enemyShip2.getSpeed() + 4);
            enemyShip3.setSpeed(enemyShip3.getSpeed() + 4);
            enemyShip4.setSpeed(enemyShip4.getSpeed() + 4);
        }
        enemyShip1.update(player.getSpeed());
        enemyShip2.update(player.getSpeed());
        enemyShip3.update(player.getSpeed());
        if(screenX > 1000) {
            enemyShip4.update(player.getSpeed());
        }
        for(int i=0; i<dustList.size();i++)
        {
            dustList.get(i).update(player.getSpeed());
        }

        if(!gameEnded){
            distance += player.getSpeed();
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

    }
    private void draw(){

        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,0,0,0));

            paint.setColor(Color.argb(255, 255, 255, 255));
            for(int i=0;i<dustList.size();i++){
                canvas.drawPoint(dustList.get(i).getX(), dustList.get(i).getY(), paint);
            }

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint
            );

            canvas.drawBitmap(enemyShip1.getEnemyBitmap(),
                    enemyShip1.getX(),
                    enemyShip1.getY(),
                    paint
            );
            canvas.drawBitmap(enemyShip2.getEnemyBitmap(),
                    enemyShip2.getX(),
                    enemyShip2.getY(),
                    paint
            );
            canvas.drawBitmap(enemyShip3.getEnemyBitmap(),
                    enemyShip3.getX(),
                    enemyShip3.getY(),
                    paint
            );
            if(screenX > 1000) {
                canvas.drawBitmap(enemyShip4.getEnemyBitmap(),
                        enemyShip4.getX(),
                        enemyShip4.getY(),
                        paint
                );
            }


            if(!gameEnded){


                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255,255,255,255));
                paint.setTextSize(25);


                canvas.drawText("Best Record :- \tTime:"+ formatTime(fastestTime ) + "s", 10, 20, paint);
                canvas.drawText("Distance:" + highestDistance + " KM", screenX/3 +200, 20, paint);
                canvas.drawText("Time:" + formatTime(timeTaken)  + "s", (screenX-200), 20, paint);


                canvas.drawText("Shield:" + player.getShieldStrength(), 10, screenY - 20, paint);
                canvas.drawText("Distance:" + distance / 1000 + " KM", screenX / 3 + 200, screenY - 20, paint);
                canvas.drawText("Speed:" + player.getSpeed()*60 + " MPS", (screenX - 200), screenY - 20, paint);

            }
            else {
                paint.setTextSize(80);
                win.start();
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("GAME OVER", screenX/2, 200, paint);
                paint.setTextSize(35);
                canvas.drawText("Your Score", screenX/2, 300, paint);
                paint.setTextSize(25);
                canvas.drawText("Time:" + formatTime(timeTaken) + "s", screenX / 2, 350, paint);
                canvas.drawText("Distance :" + distance/1000 + " KM",screenX/2, 390, paint);
                paint.setTextSize(35);



                canvas.drawText("Highest Score", screenX/2, 450, paint);
                paint.setTextSize(25);

                canvas.drawText("Time: "+ formatTime(fastestTime) + "s", screenX/2, 540, paint);
                canvas.drawText("Distance: " + highestDistance/1000 + "KM", screenX/2, 500, paint);
                paint.setTextSize(80);
                canvas.drawText("TAP TO REPLAY!!", screenX/2, 650, paint);

                if(distance > highestDistance){
                    editor.putLong("fastestTime", timeTaken);
                    editor.putFloat("highestDistance", distance);
                    editor.commit();
                    fastestTime = timeTaken;
                    highestDistance = distance;

                }
            }
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control(){
        try{
            gameThread.sleep(17);
        }catch (InterruptedException e){
            Log.e(TAG, "control: Thread Error " + e );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                race.start();
                player.setBoosting();
                if(gameEnded){
                    startGame();
                }
                break;
        }
        return true;
    }
}
