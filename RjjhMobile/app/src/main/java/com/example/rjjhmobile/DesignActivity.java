package com.example.rjjhmobile;

import androidx.annotation.NonNull;
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
import android.os.Handler;
import android.os.Message;
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
import com.example.rjjhmobile.painter.RoomUserPainter;
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
        ImageView roomUser = findViewById(R.id.room_user);
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

        RoomUserPainter roomUserPainter = new RoomUserPainter(roomWidth,roomHeight,getScreenHeight(this),roomUser);
        Bitmap userBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.user);
        SocketConnect connect = SocketConnect.getConnect();

        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    String resString = msg.getData().getString("json");
                    JSONObject resJSON = new JSONObject(resString);
                    double userX = resJSON.getDouble("x");
                    double userY = resJSON.getDouble("y");
                    double userRotate = resJSON.getDouble("rotate");
                    Bitmap tempBitmap=userBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    roomUserPainter.printUser(userX,userY,userRotate,tempBitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject sendJSON = new JSONObject();
                    sendJSON.put("type","location");
                    while (true){
                        String res = connect.send(sendJSON.toString());
                        Message msg = new Message();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("json",res);
                        msg.setData(bundle1);
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    int getScreenHeight(Context context){
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.y;
    }
    public void OKButtonClick(View view){
        if(roomDesigner.settleObj() != -1) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    socketConnect.send(roomDesigner.toJSONMap().toString());
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
    public void deleteButtonClick(View view){
        roomDesigner.deleteObj();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                socketConnect.send(roomDesigner.toJSONMap().toString());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void turnRight(View view){
        roomDesigner.rotateObj(1);
    }
    public void turnLeft(View view){
        roomDesigner.rotateObj(-1);
    }
}