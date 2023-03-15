package com.yc.ac.utils.adgromore.log;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class LogSDK {
    public static LogSDK sInstance;
    private String mIp;
    private int mPort;

    public static LogSDK getInstance() {
        if (sInstance == null) {
            synchronized (LogSDK.class) {
                if (sInstance == null) {
                    sInstance = new LogSDK();
                }
            }
        }
        return sInstance;
    }

    public void init(String ip, int port) {
        this.mIp = ip;
        this.mPort = port;
    }

    public void send(String cvs) {
        send(mIp, mPort, cvs);
    }

    public void send(int port, String cvs) {
        send(mIp, port, cvs);
    }

    public void send(String ip, int port, String cvs) {
        DatagramSocket ds = null;
        try {
            InetAddress addr = InetAddress.getByName(ip);
            ds = new DatagramSocket();
            DatagramPacket dp;
            dp = new DatagramPacket(cvs.getBytes(), cvs.getBytes().length, addr, port);
            ds.setBroadcast(true);
            Log.d("AdLog", "----------send: "+cvs);
            ds.send(dp);
        } catch (Exception e) {
            Log.d("AdLog", "-----Exception-----send: ");
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
}


