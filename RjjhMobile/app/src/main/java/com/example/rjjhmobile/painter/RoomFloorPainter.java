package com.example.rjjhmobile.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

import com.example.rjjhmobile.util.ColorUtil;

public class RoomFloorPainter {
    private final ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private int dis;
    private int x,y;


    public RoomFloorPainter(int x, int y, int screenHeight,ImageView imageView){
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

    public void printFloor(int beginX, int beginY, int endX, int endY, int color){
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(new Rect(beginX*dis,beginY*dis,(endX+1)*dis,(endY+1)*dis),paint);
        imageView.setImageBitmap(bitmap);
    }
}
