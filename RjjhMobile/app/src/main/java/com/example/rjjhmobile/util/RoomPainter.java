package com.example.rjjhmobile.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

public class RoomPainter {
    private final ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private int dis;
    private int nowX,nowY;


    public RoomPainter(int x, int y, int screenHeight,ImageView imageView){
        this.imageView = imageView;
        dis = screenHeight / (Math.max(x,y) + 2);
        bitmap = Bitmap.createBitmap(dis*(x+2), dis*(y+2), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        paintRoomGrid(x,y);
    }
    public void paintRoomGrid(int x, int y){
        nowX = x;
        nowY = y;

        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);

        //横线
        for (int i = 1; i < x + 2; i++){
            canvas.drawLine(dis*i,dis,dis*i,dis*(y+1),paint);
        }
        //竖线
        for (int i = 1; i < y + 2; i++){
            canvas.drawLine(dis,dis*i,dis*(x+1),dis*i,paint);
        }

        imageView.setImageBitmap(bitmap);
    }
    public void colorChange(int beginX,int beginY,int endX,int endY,int color){
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(new Rect(beginX*dis,beginY*dis,(endX+1)*dis,(endY+1)*dis),paint);

        paintRoomGrid(nowX,nowY);
    }
    public void pastePicture(int beginX,int beginY,int endX,int endY,Bitmap bm){
        Rect src = new Rect(0,0,bm.getWidth(),bm.getHeight());
        Rect des = new Rect(beginX*dis,beginY*dis,(endX+1)*dis,(endY+1)*dis);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(bm,src,des,paint);
        imageView.setImageBitmap(bitmap);
    }
}
