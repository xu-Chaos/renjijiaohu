package com.example.rjjhmobile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketConnect {
    private static SocketConnect connect;
    OutputStream os;
    PrintWriter pw;
    InputStream is;
    BufferedReader br;

    private Socket socket;
    private String ip;
    private int port;
    private SocketConnect(){}
    public static SocketConnect getConnect(){
        if(connect == null){
            connect = new SocketConnect();
        }
        return connect;
    }

    public boolean connect(){
        try {
            //socket = new Socket(ip,port);
            socket = new Socket();
            InetSocketAddress address = ip != null ? new InetSocketAddress(ip, port) :
                    new InetSocketAddress(InetAddress.getByName(null), port);
            socket.connect(address, 3000);

            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String send(String msg){
        if(socket == null) return null;
        else{
            try {
                pw.println(msg);
                pw.flush();
                return br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void close(){
        try {
            br.close();
            is.close();
            os.close();
            pw.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setIP(String ip){
        this.ip = ip;
    }
    public void setPort(int port){
        this.port = port;
    }
    public String getIP(){
        return ip;
    }
    public int getPort(){
        return port;
    }
}
