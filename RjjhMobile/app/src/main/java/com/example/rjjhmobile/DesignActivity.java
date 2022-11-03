package com.example.rjjhmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlong.rep.dlroundmenuview.DLRoundMenuView;
import com.dlong.rep.dlroundmenuview.Interface.OnMenuClickListener;
import com.example.rjjhmobile.painter.RoomDesigner;
import com.example.rjjhmobile.painter.RoomFloorPainter;
import com.example.rjjhmobile.painter.RoomGridPainter;
import com.example.rjjhmobile.painter.RoomImagePainter;
import com.example.rjjhmobile.util.AssetsReader;

public class DesignActivity extends AppCompatActivity {
    RoomDesigner roomDesigner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        ImageView roomGrid = findViewById(R.id.room_grid);
        ImageView roomFloor = findViewById(R.id.room_floor);
        ImageView roomImg = findViewById(R.id.room_image);
        TextView furniture = findViewById(R.id.furniture_name);

        roomDesigner = new RoomDesigner(10,8,getScreenHeight(DesignActivity.this),roomGrid,roomFloor,roomImg,furniture);
        roomDesigner.createObj(1,DesignActivity.this);
        roomDesigner.moveObj(4,5);
        roomDesigner.settleObj();
        roomDesigner.createObj(2,DesignActivity.this);

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
    }

    int getScreenHeight(Context context){
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.y;
    }
    public void OKButtonClick(View view){
        roomDesigner.settleObj();
    }
    public void deleteButtonClick(View view){
        roomDesigner.deleteObj();
    }
    public void newObjClick(View view){
        roomDesigner.createObj(1,DesignActivity.this);
    }
    public void turnRight(View view){
        roomDesigner.rotateObj(1);
    }
    public void turnLeft(View view){
        roomDesigner.rotateObj(-1);
    }
    public void test(View view){
        System.out.println(roomDesigner.toJSONMap());
    }
}