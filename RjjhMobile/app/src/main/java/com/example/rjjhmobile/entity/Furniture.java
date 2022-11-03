package com.example.rjjhmobile.entity;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.json.JSONObject;

public class Furniture {
    private int x;
    private int y;
    private int rotate;
    private Bitmap img;
    private int width;
    private int height;
    private int classID;
    private String name;

    public Furniture cloneObj(){
        Furniture furniture = new Furniture();
        furniture.setX(x);
        furniture.setY(y);
        furniture.setRotate(rotate);
        furniture.setImg(img);
        furniture.setWidth(width);
        furniture.setHeight(height);
        furniture.setClassID(classID);
        furniture.setName(name);
        return furniture;
    }

    public void rotate90(int w,int h,int r){
        double d = 1.0*width/2 - 1.0*height/2;
        int xx = x + (int)d;
        int yy = y - (int)d;

        Matrix matrix = new Matrix();
        matrix.preRotate(r*90);
        img = Bitmap.createBitmap(img,0,0,img.getWidth(),img.getHeight(),matrix,false);
        int t = width;
        width = height;
        height = t;

        x = Math.max(1,xx);
        y = Math.max(1,yy);
        x = Math.min(x,w-width+1);
        y = Math.min(y,h-height+1);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "x=" + x +
                ", y=" + y +
                ", rotate=" + rotate +
                ", img=" + img +
                ", width=" + width +
                ", height=" + height +
                ", classID=" + classID +
                ", name='" + name + '\'' +
                '}';
    }
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("x",x);
            jsonObject.put("y",y);
            jsonObject.put("rotate",rotate);
            jsonObject.put("classID", classID);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
