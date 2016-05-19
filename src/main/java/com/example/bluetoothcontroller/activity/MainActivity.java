package com.example.bluetoothcontroller.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothcontroller.R;
import com.example.bluetoothcontroller.bluetoothcontroller.MyBlueToothController;
import com.example.bluetoothcontroller.thread.ConnectThread;
import com.example.bluetoothcontroller.view.ControlView;
import com.example.bluetoothcontroller.view.TopItemView;
import com.example.bluetoothcontroller.view.TopView;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_TRUN_ON_BLUETOOTH = 0;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    public static final int SEND_DATA = 1;
    public static final int CLOSE_CONNECT = 2;
    public static final int CLOSE_CONNECT_ERROR = 3;
    TopView mTopView;
    ControlView mControlView;
    private Toast mToast;
    boolean openBluetooth,connectDevice,startConrol;


    MyBlueToothController mBlueToothController;
    ConnectThread mConnectThread;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_DATA:
                    showToast("成功连接设备");
                    mTopView.setItem2Selected();
                    connectDevice = true;
                    mControlView.setConnectDevice(true);
                    break;
                case CLOSE_CONNECT:
                    showToast("断开连接");
                    mControlView.setConnectDevice(false);
                    break;
                case CLOSE_CONNECT_ERROR:
                    showToast("未知错误");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        findView();
        mBlueToothController = MyBlueToothController.getController();
        if (mBlueToothController.getBlueToothStatus()){
            mTopView.setItem1Selected();
            openBluetooth = true;
        }
        setClick();
    }


    private void setClick() {
        setTopViewClick();
        setControlViewClick();

    }

    /**
     * 为ControlView实现Click
     */
    private void setControlViewClick() {
        mControlView.setControlScrollListener(new ControlView.ControlScrollListener() {
            @Override
            public void onScroll(String str) {
                if (startConrol == true) {
                    switch (str) {
                        case "left1":
                            mConnectThread.sendData(0x4F);
                            break;
                        case "right1":
                            mConnectThread.sendData(0x2F);
                            break;
                        case "top1":
                            mConnectThread.sendData(0x1F);
                            break;
                        case "bottom1":
                            mConnectThread.sendData(0x3F);
                            break;
                        case "left2":
                            mConnectThread.sendData(0x8F);
                            break;
                        case "right2":
                            mConnectThread.sendData(0x6F);
                            break;
                        case "top2":
                            mConnectThread.sendData(0x5F);
                            break;
                        case "bottom2":
                            mConnectThread.sendData(0x7F);
                            break;
                        case "stop":
                            mConnectThread.sendData(0x00);
                }
                }
            }
        });
        mControlView.setControlButtonClickListener(new ControlView.ControlButtonClickListener() {
            @Override
            public void click(int viewId) {
                if (connectDevice){
                    switch (viewId){
                        case R.id.pwm_button1:
                            mConnectThread.sendData(0xA0);
                            break;
                        case R.id.pwm_button2:
                            mConnectThread.sendData(0xB0);
                            break;
                        case R.id.pwm_button3:
                            mConnectThread.sendData(0xC0);
                            break;
                        case R.id.pwm_button4:
                            mConnectThread.sendData(0xD0);
                            break;
                        case R.id.pwm_button5:
                            mConnectThread.sendData(0xE0);
                            break;
                        case R.id.start_control:
                            mConnectThread.sendData(0x10);
                            mConnectThread.sendData(0xA0);
                            startConrol=true;
                            break;
                        case R.id.stop_control:
                            mConnectThread.sendData(0xF0);
                            startConrol = false;
                            break;
                    }
                }else {
                    showToast("未连接蓝牙设备");
                }

            }
        });
    }

    /**
     * 为TopView实现Click
     */
    private void setTopViewClick() {
        mTopView.setClickListner(new TopView.ICallbackListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.key_bluetooth:
                        if (openBluetooth == true) {
                            mBlueToothController.turnOffBlueTooth();
                            mTopView.setItem1UnSelected();
                            openBluetooth = false;
                            showToast("关闭蓝牙");
                        } else {
                            mBlueToothController.turnOnBlueTooth(MainActivity.this, REQUEST_TRUN_ON_BLUETOOTH);
                        }
                        break;
                    case R.id.link_device:
                        if (connectDevice == false) {
                            if (openBluetooth == false) {
                                showToast("请打开蓝牙服务");
                                break;
                            }
                            Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                            startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                        }
                        break;
                    case R.id.break_device:
                        if (openBluetooth == false) {
                            showToast("请打开蓝牙服务");
                            break;
                        }
                        if (connectDevice == false) {
                            showToast("未连接蓝牙设备");
                            break;
                        }
                        if (connectDevice == true) {
                            mConnectThread.disConnect();
                            connectDevice = false;
                            mTopView.setItem2UnSelected();
                        }
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TRUN_ON_BLUETOOTH){
            if (resultCode == RESULT_OK){
                mTopView.setItem1Selected();
                showToast("打开成功");
                openBluetooth = true;
            }else {
                mTopView.setItem1UnSelected();
                showToast("打开失败");
                openBluetooth = false;
            }
        }else if (requestCode == REQUEST_CONNECT_DEVICE){
            if (resultCode == RESULT_OK){

                BluetoothDevice device = data.getParcelableExtra("device");
                connectDevice(device);
            }
        }

    }

    /**
     * 连接设备
     * @param device
     */
    private void connectDevice(BluetoothDevice device) {
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            mConnectThread = new ConnectThread(mBlueToothController,socket,handler);
            mConnectThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void findView() {
        mTopView = (TopView) findViewById(R.id.top_view);
        mControlView = (ControlView) findViewById(R.id.control);
    }
    private void showToast(String text) {
        if( mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }
        else {
            mToast.setText(text);
        }
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
