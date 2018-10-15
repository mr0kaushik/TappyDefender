package com.app_services.mr_kaushik.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class PlayerShip {
    private int shieldStrength;
    private Rect hitBox;
    private final int GRAVITY = -15;
    private int maxY;
    private int minY;
    private final int MAX_SPEED = 35;
    private final int MIN_SPEED = 1;
    private boolean boosting;
    private Bitmap bitmap;
    private int x, y;
    private int speed = 0;
    private Context context;
    public PlayerShip(Context context, int screenX, int screenY){
        shieldStrength = 5;
        boosting = false;
        x = 50;
        y = 50;
        this.context = context;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        maxY = screenY - bitmap.getHeight();
        Log.d(TAG, "PlayerShip: Height " + bitmap.getHeight() );
        Log.d(TAG, "PlayerShip: width " + bitmap.getWidth());
        minY = 0;
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }

    public void setBoosting(){
        boosting = true;
    }
    public void stopBoosting(){
        boosting = false;
    }

    public void reduceStrength(){
        shieldStrength--;
    }

    public void update(){
        //x++;

        if(boosting){
            speed += 5;

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.race_ship_2);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/3, false);
        }
        else {
            speed -= 8;
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        }
        if(speed > MAX_SPEED)
        {
            speed = MAX_SPEED;
        }
        if(speed < MIN_SPEED){
            speed = MIN_SPEED;
        }

        y -= speed + GRAVITY;
        if(y < minY){
            y = minY;
        }
        if(y > maxY){
            y = maxY;
        }

        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

    }

    /*
    public Bitmap setBitmap(int bitmap){
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), bitmap);
    }
    */
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
