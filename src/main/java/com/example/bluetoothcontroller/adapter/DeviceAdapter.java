package com.example.bluetoothcontroller.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 杨胜 on 2016/2/11.
 */
public class DeviceAdapter extends BaseAdapter{
    Context mContext;
    List<BluetoothDevice> mDevices;

    public DeviceAdapter(List<BluetoothDevice> devices, Context context){
        mDevices = devices;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return mDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
            viewHolder.textView1 = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.textView2 = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView1.setText(getItem(position).getName());
        viewHolder.textView2.setText(getItem(position).getAddress());
        return convertView;
    }
    class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
}
