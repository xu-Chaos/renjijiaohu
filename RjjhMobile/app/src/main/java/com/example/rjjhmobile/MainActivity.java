package com.example.rjjhmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText ipInput;
    EditText portInput;
    TextView connectState;
    SocketConnect connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ipInput = findViewById(R.id.ip_input);
        portInput = findViewById(R.id.port_input);
        connectState = findViewById(R.id.connect_state);
        connect = SocketConnect.getConnect();
    }

    public void connect(View view){
        try {
            connect.setIP(ipInput.getText().toString());
            connect.setPort(Integer.parseInt(portInput.getText().toString()));
        }catch (Exception e){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请输入正确ip/port")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    }).create();
            alertDialog.show();
            return;
        }

        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == 0){
                    connectState.setText("已连接");
                    connectState.setTextColor(Color.GREEN);
                }
                else if(msg.arg1 == -1){
                    connectState.setText("连接失败");
                    connectState.setTextColor(Color.RED);
                }
                else{
                    connectState.setText("未知错误");
                }
            }
        };


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                if(connect.connect()){
                    msg.arg1 = 0;
                }
                else {
                    msg.arg1 = -1;
                }
                handler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        connectState.setText("正在连接...");
    }
    public void disconnect(View view){
        connect.close();
    }
    public void toDesignPage(View view){
        int width;
        int height;
        try {
            EditText rowInput = findViewById(R.id.row_input);
            EditText columnInput = findViewById(R.id.column_input);
            width = Integer.parseInt(rowInput.getText().toString());
            height = Integer.parseInt(columnInput.getText().toString());

            Intent intent = new Intent();
            intent.putExtra("width",width);
            intent.putExtra("height",height);
            intent.setClass(this,DesignActivity.class);
            startActivity(intent);
        }catch (Exception e){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请输入正确的房间大小")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    }).create();
            alertDialog.show();
        }
    }
}