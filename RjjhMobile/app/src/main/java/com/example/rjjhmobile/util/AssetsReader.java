package com.example.rjjhmobile.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsReader {
    private Context context;

    public AssetsReader(Context context){
        this.context = context;
    }
    public Bitmap readImage(String fileName){
        Bitmap bitmap = null;
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public JSONObject readJSON(String fileName){
        JSONObject json = new JSONObject();
        AssetManager assetManager = context.getAssets();
        try {
            InputStreamReader inputReader = new InputStreamReader(assetManager.open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String res = "";
            while((line = bufReader.readLine()) != null)
                res += line;
            json = new JSONObject(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
