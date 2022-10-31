package com.example.rjjhmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

import com.example.rjjhmobile.util.RoomPainter;

import java.io.InputStream;

public class DesignActivity extends AppCompatActivity {

    ImageView roomMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        roomMap = findViewById(R.id.room_map);

        RoomPainter roomPainter = new RoomPainter(20,30,getScreenHeight(DesignActivity.this),roomMap);
        roomPainter.colorChange(1,1,2,4,Color.GREEN);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        roomPainter.pastePicture(4,4,5,4,bitmap);
    }

    int getScreenHeight(Context context){
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        System.out.println(point.y);
        return point.y;
    }


}