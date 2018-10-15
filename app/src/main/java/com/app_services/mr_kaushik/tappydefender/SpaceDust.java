package com.app_services.mr_kaushik.tappydefender;

import java.util.Random;

public class SpaceDust {
    private int x, y;
    private int speed;
    private int maxX;
    private int maxY;

    public SpaceDust(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;
//        minX = 0;
//        minY = 0;

        Random speedGenerator = new Random();
        speed = speedGenerator.nextInt(10);

        x = speedGenerator.nextInt(maxX);
        y = speedGenerator.nextInt(maxY);
    }

    public void update(int playerSpeed){
        x -= playerSpeed;
        x -= speed;
        if(x < 0){
            x = maxX;
            Random speedGenerator = new Random();
            y = speedGenerator.nextInt(maxY);
            speed = speedGenerator.nextInt(25);
        }
    }
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
