package com.example.rjjhmobile.util;

import android.widget.ImageView;

public class RoomDesigner {
    private RoomPainter roomPainter;
    private int flag[][];

    public RoomDesigner(int x, int y, int screenHeight, ImageView imageView){
        roomPainter = new RoomPainter(x,y,screenHeight,imageView);
        flag = new int[x][y];
    }

    //输入左上角那个点的x，y坐标，objID，方向，返回一个编号（唯一标识）
    public int createObj(int x,int y,int objID,int direction){
        return 0;
    }
    public void moveObj(int id,int x,int y){

    }
    public void rotateObj(int id,int rotate){

    }
}
