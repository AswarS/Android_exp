package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;

    private ArrayList<Sprite> sprites = new ArrayList<>();
    private ArrayList<Location> locates = new ArrayList<>(); //存放地鼠出现位置
    float xTouch = -1,yTouch=-1; //点击坐标
    public GameView(Context context) {
        super(context);
        surfaceHolder=this.getHolder();
        surfaceHolder.addCallback(this);//回调接口

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xTouch=event.getX();
                yTouch=event.getY();
                return false;
            }
        });


    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if(null==drawThread){
            sprites = new ArrayList<>();
            for(int i=0;i<10;i++)//添加老鼠
                sprites.add(new Sprite(R.drawable.mouse_icon));

            drawThread = new DrawThread();
            drawThread.start();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//        if(null!=drawThread)
//        {
//            drawThread.stopThread();
//            drawThread=null;
//        }
    }
    //地鼠出现位置
    private class Location{
        int x,y;
        public Location(int x,int y){
            this.x =x;this.y=y;
        }
    }

    private class Sprite {
        private int resouceID;
        private int x,y;//位置坐标
        private double direction;//前进方向与x轴的夹角，以弧度为单位

        public Sprite(int mouse_icon) {
            this.resouceID = resouceID;
            x=(int)(Math.random()*getWidth()/2 + getWidth()/4);
            y=(int)(Math.random()*getHeight()/2+ getWidth()/4);
        }
        public void move(){
            x+=15*Math.cos(direction);
            if(x<0)x=getWidth();
            else if(x>getWidth())x=0;

            y+=15*Math.sin(direction);
            if(y<0)y=getHeight();
            else if(y>getWidth())y=0;

            if(Math.random()<0.05)direction=Math.random()*2*Math.PI;
        }

        public boolean Draw(Canvas canvas){
            boolean isDraw = false;
            Paint paint = new Paint();
            paint.setDither(true);//防抖动
            paint.setFilterBitmap(true);//抗锯齿

            //获取地鼠图标
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mouse_icon);
            if(Math.random()<0.1){
                //出现地鼠
                canvas.drawBitmap(bitmap,x,y,paint);
                isDraw=true;
            }
            return  isDraw;
        }

    }

    private class DrawThread extends Thread{
        private boolean beAlive=true;

        private boolean Count()//判断是否点击了地鼠
        {
            for (GameView.Location location: locates) {
                if ((Math.abs(location.x - GameView.this.xTouch) < 100) && (Math.abs(location.y - GameView.this.yTouch) < 100))
                    return true;
            }
            return false;
        }
        @Override
        public void run() {
            super.run();
            int hittedCount = 0;

            // beAlive = true;//循环永久执行直到其它函数把beAlive设置为false
            while (beAlive) {
                Canvas canvas = null;
                boolean isDraw = false;

                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                //积分板
                Paint paint = new Paint();
                paint.setTextSize(50);
                paint.setColor(Color.BLACK);
                canvas.drawText("你打中了" + hittedCount + "个", 100, 100, paint);

                //每一轮开始先清空地鼠出现的位置
                locates.clear();
                for (Sprite sprite : sprites) {
                    isDraw = sprite.Draw(canvas);
                    if (isDraw)
                        locates.add(new GameView.Location(sprite.x, sprite.y));
                }

                surfaceHolder.unlockCanvasAndPost(canvas);

                if (xTouch > 0) {
                    if (Count()) {
                        hittedCount++;
                        xTouch = yTouch = -1;
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

