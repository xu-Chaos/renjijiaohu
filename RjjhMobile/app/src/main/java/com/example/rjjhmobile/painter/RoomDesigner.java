package com.example.rjjhmobile.painter;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rjjhmobile.entity.Furniture;
import com.example.rjjhmobile.util.AssetsReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class RoomDesigner {
    private RoomGridPainter roomGridPainter;
    private RoomFloorPainter roomFloorPainter;
    private RoomImagePainter roomImagePainter;
    private TextView furnitureName;
    private int flag[][];
    private Furniture nowSelected;
    private Map<Integer,Furniture> furnitureMap;
    private int nowID;
    private int selectID;

    public RoomDesigner(int x, int y, int screenHeight, ImageView gridView, ImageView floorView, ImageView imageView, TextView furnitureName){
        roomGridPainter = new RoomGridPainter(x,y,screenHeight,gridView);
        roomFloorPainter = new RoomFloorPainter(x,y,screenHeight,floorView);
        roomImagePainter = new RoomImagePainter(x,y,screenHeight,imageView);
        this.furnitureName = furnitureName;
        flag = new int[x][y];
        nowID = 1;

        furnitureMap = new TreeMap<>();

        roomGridPainter.paintRoomGrid(Color.rgb(128, 128, 128));

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int dis = screenHeight / (Math.max(x,y) + 2);
                int xx = (int)Math.floor(motionEvent.getX()/dis);
                int yy = (int)Math.floor(motionEvent.getY()/dis);
                if(xx > 0 && yy > 0 && xx <= x && yy <= y){
                    if(nowSelected == null){
                        selectObj(xx,yy);
                    }
                }
                return false;
            }
        });
    }

    public boolean createObj(int objID, Context context){
        if(nowSelected != null){
            return false;
        }
        AssetsReader assetsReader = new AssetsReader(context);
        JSONObject target = new JSONObject();
        Furniture furniture = new Furniture();
        try {
            JSONArray furnitureArray = assetsReader.readJSON("FurnitureList.json").getJSONArray("furniture");
            for (int i = 0; i < furnitureArray.length(); i++){
                JSONObject temp = furnitureArray.getJSONObject(i);
                if(temp.getInt("id") == objID){
                    target = temp;
                    break;
                }
            }
            furniture.setClassID(objID);
            furniture.setName(target.getString("name"));
            furniture.setWidth(target.getInt("width"));
            furniture.setHeight(target.getInt("height"));
            furniture.setImg(assetsReader.readImage(target.getString("img")));
        }catch (Exception e){
            e.printStackTrace();
        }
        furniture.setX(1);
        furniture.setY(1);
        furniture.setRotate(0);
        nowSelected = furniture;
        paintNowSelect();
        selectID = 0;
        furnitureName.setText(nowSelected.getName());
        return true;
    }
    public Furniture getNowSelected(){
        return nowSelected;
    }
    public boolean moveObj(int x,int y){
        if (nowSelected == null) return false;
        if(x > 0 && y > 0 && x+nowSelected.getWidth() < flag.length+2 && y+nowSelected.getHeight() < flag[0].length+2){
            nowSelected.setX(x);
            nowSelected.setY(y);
            paintNowSelect();
            return true;
        }
        else return false;
    }
    public void rotateObj(int r){
        if(nowSelected != null) {
            nowSelected.rotate90(flag.length, flag[0].length, r);
            paintNowSelect();
            nowSelected.setRotate(((nowSelected.getRotate()+r)%4+4)%4);
        }
    }
    public void selectObj(int x,int y){
        if(flag[x-1][y-1] != 0) {
            selectID = flag[x - 1][y - 1];
            nowSelected = furnitureMap.get(selectID).cloneObj();
            paintNowSelect();
            furnitureName.setText(nowSelected.getName());
        }
    }
    public int settleObj(){
        if(nowSelected == null) return -1;
        for (int i = 0; i < nowSelected.getWidth(); i++){
            for (int j = 0; j < nowSelected.getHeight(); j++){
                int tx = nowSelected.getX() + i;
                int ty = nowSelected.getY() + j;
                if(flag[tx-1][ty-1] != 0 && flag[tx-1][ty-1] != selectID){
                    return -1;
                }
            }
        }
        if (selectID == 0){
            furnitureMap.put(nowID,nowSelected);
            selectID = nowID;
            nowID++;
        }
        else {
            Furniture old = furnitureMap.get(selectID);
            for (int i = 0; i < old.getWidth(); i++){
                for (int j = 0; j < old.getHeight(); j++){
                    int tx = old.getX() + i;
                    int ty = old.getY() + j;
                    flag[tx-1][ty-1] = 0;
                }
            }
            furnitureMap.put(selectID,nowSelected);
        }
        for (int i = 0; i < nowSelected.getWidth(); i++){
            for (int j = 0; j < nowSelected.getHeight(); j++){
                int tx = nowSelected.getX() + i;
                int ty = nowSelected.getY() + j;
                flag[tx-1][ty-1] = selectID;
            }
        }
        roomFloorPainter.clean();
        nowSelected = null;
        return selectID = 0;
    }
    public void deleteObj(){
        if(selectID == 0){
            nowSelected = null;
        }
        else{
            Furniture temp = furnitureMap.get(selectID);
            for (int i = 0; i < temp.getWidth(); i++){
                for (int j = 0; j < temp.getHeight(); j++){
                    int tx = temp.getX() + i;
                    int ty = temp.getY() + j;
                    flag[tx-1][ty-1] = 0;
                }
            }
            nowSelected = null;
            furnitureMap.remove(selectID);
            selectID = 0;
        }
        paintNowSelect();
    }
    private void paintNowSelect() {
        roomFloorPainter.clean();
        roomImagePainter.clean();

        for (Map.Entry<Integer, Furniture> entry : furnitureMap.entrySet()) {
            int mapKey = entry.getKey();
            Furniture furniture = entry.getValue();
            if (mapKey != selectID && furniture != null) {
                roomImagePainter.pastePicture(furniture.getX(), furniture.getY(),
                        furniture.getX() + furniture.getWidth() - 1, furniture.getY() + furniture.getHeight() - 1,
                        furniture.getImg());
            }
        }

        if (nowSelected != null) {
            //color
            for (int i = 0; i < nowSelected.getWidth(); i++) {
                for (int j = 0; j < nowSelected.getHeight(); j++) {
                    int tx = nowSelected.getX() + i;
                    int ty = nowSelected.getY() + j;
                    if (flag[tx - 1][ty - 1] == 0 || flag[tx - 1][ty - 1] == selectID) {
                        //green
                        roomFloorPainter.printFloor(tx, ty, tx, ty, Color.argb(150, 102, 204, 0));
                    } else {
                        //red
                        roomFloorPainter.printFloor(tx, ty, tx, ty, Color.argb(150, 204, 0, 0));
                    }
                }
            }

            //img
            roomImagePainter.pastePicture(nowSelected.getX(), nowSelected.getY(),
                    nowSelected.getX() + nowSelected.getWidth() - 1, nowSelected.getY() + nowSelected.getHeight() - 1,
                    nowSelected.getImg());
        }
    }
    public JSONObject toJSONMap(){
        JSONObject json = new JSONObject();
        try {
            json.put("roomWidth",flag.length);
            json.put("roomHeight",flag[0].length);
            JSONArray array = new JSONArray();
            for (Map.Entry<Integer, Furniture> entry : furnitureMap.entrySet()) {
                int mapKey = entry.getKey();
                JSONObject furnitureObj = entry.getValue().toJSONObject();
                furnitureObj.put("id",mapKey);
                array.put(furnitureObj);
            }
            json.put("furniture",array);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }
}
