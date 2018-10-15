package com.app_services.mr_kaushik.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class EnemyShip {
    private Rect hitBox;
    private Bitmap enemyBitmap;
    private int x, y;
    private int speed = 3;
    private int maxX, minX;
    private int maxY;

    EnemyShip(Context context, int screenX, int screenY){

        Random speedGenerator = new Random();
        int selectBitmap = speedGenerator.nextInt(3);

        switch (selectBitmap){
            case 0:
                enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
                break;
            case 1:
                enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
                break;
            case 2:
                enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
                break;
        }
        scaleBitmap(screenX);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
       // minY = 0;

        speed = speedGenerator.nextInt(6) + 20;

        x = screenX;
        y = speedGenerator.nextInt(maxY) - enemyBitmap.getHeight();
        hitBox = new Rect(x, y, enemyBitmap.getWidth(), enemyBitmap.getHeight());
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public Bitmap getEnemyBitmap() {
        return enemyBitmap;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getY() {
        return y;
    }

    public void update(int playerSpeed){
         x -= playerSpeed;
         x -= speed;
         if(x < minX - enemyBitmap.getWidth()){
             Random speedGenerator = new Random();
             speed = speedGenerator.nextInt(15) + 20;
             x = maxX;
             y = speedGenerator.nextInt(maxY) - enemyBitmap.getHeight();
         }

        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + enemyBitmap.getWidth();
        hitBox.bottom = y + enemyBitmap.getHeight();
    }

    public void scaleBitmap(int screenX){
        if(screenX < 10000){
            enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap, enemyBitmap.getWidth()/3, enemyBitmap.getHeight()/3, false);
        }
        else if(screenX < 1200){
            enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap, enemyBitmap.getWidth()/2, enemyBitmap.getHeight()/2, false);
        }
    }

}
