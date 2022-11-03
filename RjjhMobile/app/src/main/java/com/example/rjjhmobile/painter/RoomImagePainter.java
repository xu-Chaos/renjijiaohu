package com.example.rjjhmobile.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

public class RoomImagePainter {
    private final ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private int dis;
    private int x,y;


    public RoomImagePainter(int x, int y, int screenHeight, ImageView imageView){
        this.imageView = imageView;
        this.x = x;
        this.y = y;
        dis = screenHeight / (Math.max(x,y) + 2);

        clean();
    }

    public void clean(){
        bitmap = Bitmap.createBitmap(dis*(x+2), dis*(y+2), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);
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
