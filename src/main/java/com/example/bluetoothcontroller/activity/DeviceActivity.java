package com.example.bluetoothcontroller.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bluetoothcontroller.R;
import com.example.bluetoothcontroller.adapter.DeviceAdapter;
import com.example.bluetoothcontroller.bluetoothcontroller.MyBlueToothController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨胜 on 2016/4/18.
 */
public class DeviceActivity extends Activity {

    ListView listView;
    TextView mTextView;
    ProgressBar mProgressBar;
    MyBlueToothController mController;
    List<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    DeviceAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.v("bluetoothFound","start to discovery");
                mTextView.setText("开始查找设备...");
                mProgressBar.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                mDeviceList.clear();
                adapter.notifyDataSetChanged();
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                mTextView.setText("搜索到的设备");
                mProgressBar.setVisibility(View.GONE);

            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device);
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_device_layout);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mController = MyBlueToothController.getController();
        registerReceiver(receiver,filter);
        initUI();
        adapter = new DeviceAdapter(mDeviceList,this);
        listView.setAdapter(adapter);
        mController.findDevice();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("device",device);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        mController.cancelDiscovery();
        finish();
        super.onBackPressed();
    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.find_device_list);
        mTextView = (TextView) findViewById(R.id.my_text);
        mProgressBar = (ProgressBar) findViewById(R.id.refresh);
        mTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

}
