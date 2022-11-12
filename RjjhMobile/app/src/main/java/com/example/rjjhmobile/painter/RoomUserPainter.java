package com.example.rjjhmobile.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.example.rjjhmobile.util.BitmapUtil;

public class RoomUserPainter {
    private final ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private int width;
    private int height;
    private int dis;
    public RoomUserPainter(int x, int y, int screenHeight,ImageView imageView){
        this.imageView = imageView;
        dis = screenHeight / (Math.max(x,y) + 2);
        width = dis * x;
        height = dis * y;
    }

    public void clean(){
        bitmap = Bitmap.createBitmap(dis*2+width, dis*2+height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    public void printUser(double x, double y, double rotate, Bitmap bmp){
        clean();

        int centerX = (int)(x * width + dis);
        int centerY = (int)(y * height + dis);

        Bitmap bm = BitmapUtil.rotateBitmap(bmp,(float)rotate);

        int radius = 50;
        Rect src = new Rect(0,0,bm.getWidth(),bm.getHeight());
        Rect des = new Rect(centerX-radius,centerY-radius,centerX+radius,centerY+radius);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(bm,src,des,paint);
        imageView.setImageBitmap(bitmap);
    }
}
