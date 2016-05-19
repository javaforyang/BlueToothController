package com.example.bluetoothcontroller.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.example.bluetoothcontroller.activity.MainActivity;
import com.example.bluetoothcontroller.bluetoothcontroller.MyBlueToothController;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 杨胜 on 2016/2/25.
 */
public class ConnectThread extends Thread {
   // private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private  BluetoothSocket mmSocket;
    private  BluetoothDevice mmDevice;
    private MyBlueToothController mmController;
    private OutputStream tmpOut;
    Handler mHandler;

    public ConnectThread(MyBlueToothController controller, BluetoothSocket socket, Handler handler) {
        mmController = controller;
        mmSocket = socket;
        mHandler = handler;
    }


    @Override
    public void run() {
        mmController.cancelDiscovery();
        try {
            mmSocket.connect();
            if (mmSocket.isConnected()) {
                mHandler.obtainMessage(MainActivity.SEND_DATA).sendToTarget();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendData(int msgBuffer) {
        try {
            tmpOut.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disConnect() {
        try {
            tmpOut.close();
            mmSocket.close();
            mHandler.sendEmptyMessage(MainActivity.CLOSE_CONNECT);
        } catch (IOException e) {
            mHandler.sendEmptyMessage(MainActivity.CLOSE_CONNECT_ERROR);
            e.printStackTrace();
        }

    }

    public boolean isConnect() {
        return mmSocket.isConnected();
    }
}
