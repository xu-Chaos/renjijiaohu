package com.example.rjjhmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlong.rep.dlroundmenuview.DLRoundMenuView;
import com.dlong.rep.dlroundmenuview.Interface.OnMenuClickListener;
import com.example.rjjhmobile.entity.Furniture;
import com.example.rjjhmobile.entity.FurnitureAdapter;
import com.example.rjjhmobile.painter.RoomDesigner;
import com.example.rjjhmobile.painter.RoomFloorPainter;
import com.example.rjjhmobile.painter.RoomGridPainter;
import com.example.rjjhmobile.painter.RoomImagePainter;
import com.example.rjjhmobile.util.AssetsReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DesignActivity extends AppCompatActivity {
    RoomDesigner roomDesigner;
    SocketConnect socketConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int roomWidth = bundle.getInt("width");
        int roomHeight = bundle.getInt("height");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        socketConnect = SocketConnect.getConnect();

        ImageView roomGrid = findViewById(R.id.room_grid);
        ImageView roomFloor = findViewById(R.id.room_floor);
        ImageView roomImg = findViewById(R.id.room_image);
        TextView furniture = findViewById(R.id.furniture_name);

        roomDesigner = new RoomDesigner(roomWidth,roomHeight,getScreenHeight(DesignActivity.this),roomGrid,roomFloor,roomImg,furniture);

        DLRoundMenuView roundButton = findViewById(R.id.round_button);
        roundButton.setOnMenuClickListener(new OnMenuClickListener() {
            @Override
            public void OnMenuClick(int i) {
                if(i >= 0 && roomDesigner.getNowSelected() != null){
                    int x = roomDesigner.getNowSelected().getX();
                    int y = roomDesigner.getNowSelected().getY();
                    int[] dx = new int[]{0,1,0,-1};
                    int[] dy = new int[]{-1,0,1,0};
                    roomDesigner.moveObj(x+dx[i],y+dy[i]);
                }
            }
        });

        List<Furniture> furnitureList = new ArrayList<>();
        AssetsReader assetsReader = new AssetsReader(this);
        JSONObject furnitureClass = assetsReader.readJSON("FurnitureList.json");
        try {
            JSONArray furnitureArray = furnitureClass.getJSONArray("furniture");
            for (int i = 0; i < furnitureArray.length(); i++){
                JSONObject jsonItem = furnitureArray.getJSONObject(i);
                Furniture furnitureItem = new Furniture();
                furnitureItem.setName(jsonItem.getString("name"));
                furnitureItem.setClassID(jsonItem.getInt("id"));
                furnitureItem.setWidth(jsonItem.getInt("width"));
                furnitureItem.setHeight(jsonItem.getInt("height"));
                furnitureItem.setImg(assetsReader.readImage(jsonItem.getString("img")));
                furnitureList.add(furnitureItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.furniture_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FurnitureAdapter adapter = new FurnitureAdapter(furnitureList);
        adapter.setAttribute(roomDesigner,this);
        recyclerView.setAdapter(adapter);
    }

    int getScreenHeight(Context context){
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.y;
    }
    public void OKButtonClick(View view){
        if(roomDesigner.settleObj() != -1){
            socketConnect.send(roomDesigner.toJSONMap().toString());
        }
    }
    public void deleteButtonClick(View view){
        roomDesigner.deleteObj();
        socketConnect.send(roomDesigner.toJSONMap().toString());
    }
    public void turnRight(View view){
        roomDesigner.rotateObj(1);
    }
    public void turnLeft(View view){
        roomDesigner.rotateObj(-1);
    }
}