package com.example.bluetoothcontroller.bluetoothcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨胜 on 2016/2/11.
 */
public class MyBlueToothController {

    private BluetoothAdapter mAdapter;
    private static MyBlueToothController mController = null;

    public MyBlueToothController(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 查看手机是否支持蓝牙
     * @return true,支持
     * @return false,不支持
     */
    public boolean isSupportBlueTooth() {
        if (mAdapter != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 打开蓝牙可见性
     * @param context
     */
    public void enableVisibly(Context context){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);
    }

    /**
     * 查找设备
     */
    public void findDevice(){
        assert (mAdapter != null);
        mAdapter.startDiscovery();
    }

    /**
     * 获取蓝牙状态
     * @return true,打开
     * @return  false,关闭
     */
    public boolean getBlueToothStatus(){
        assert (mAdapter != null);
        return mAdapter.isEnabled();
    }

    /**
     * 取消搜索
     */
    public void cancelDiscovery(){
        mAdapter.cancelDiscovery();
    }

    /**
     * 打开蓝牙
     * @param activity
     * @param requestCode
     */
    public void turnOnBlueTooth(Activity activity,int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 关闭蓝牙
     */
    public void turnOffBlueTooth(){
        mAdapter.disable();
    }
    /**
     * 获取绑定设备
     * @return
     */
    public List<BluetoothDevice> getBondedDeviceList() {
        return new ArrayList<>(mAdapter.getBondedDevices());
    }
    public static MyBlueToothController getController(){
        if (mController == null){
            mController = new MyBlueToothController();
        }
        return mController;
    }
}
