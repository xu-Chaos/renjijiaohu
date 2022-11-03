package com.example.rjjhmobile.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

public class RoomGridPainter {
    private final ImageView imageView;
    private final Bitmap bitmap;
    private int dis;
    private int x,y;


    public RoomGridPainter(int x, int y, int screenHeight,ImageView imageView){
        this.imageView = imageView;
        this.x = x;
        this.y = y;
        dis = screenHeight / (Math.max(x,y) + 2);
        bitmap = Bitmap.createBitmap(dis*(x+2), dis*(y+2), Bitmap.Config.ARGB_8888);
    }
    public void paintRoomGrid(int color){
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(color);

        Paint wallPaint = new Paint();
        wallPaint.setStrokeWidth(5);
        wallPaint.setColor(Color.BLACK);

        //横线
        for (int i = 2; i < x + 1; i++){
            canvas.drawLine(dis*i,dis,dis*i,dis*(y+1),paint);
        }
        //竖线
        for (int i = 2; i < y + 1; i++){
            canvas.drawLine(dis,dis*i,dis*(x+1),dis*i,paint);
        }

        //wall
        canvas.drawLine(dis,dis,dis,dis*(y+1),wallPaint);
        canvas.drawLine(dis,dis,dis*(x+1),dis,wallPaint);
        canvas.drawLine(dis*(x+1),dis,dis*(x+1),dis*(y+1),wallPaint);
        canvas.drawLine(dis,dis*(y+1),dis*(x+1),dis*(y+1),wallPaint);

        imageView.setImageBitmap(bitmap);
    }
    public void gridAppear(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 256; i++) {
                        paintRoomGrid(Color.argb(i, 128, 128, 128));
                        Thread.sleep(20);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void gridDisAppear(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 255; i >= 0; i--) {
                        paintRoomGrid(Color.argb(i, 128, 128, 128));
                        Thread.sleep(20);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
