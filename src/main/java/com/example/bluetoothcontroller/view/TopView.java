package com.example.bluetoothcontroller.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bluetoothcontroller.R;

/**
 * Created by 杨胜 on 2016/4/16.
 */
public class TopView extends LinearLayout {
    TopItemView keyBluetooth;
    TopItemView linkDevice;
    TopItemView breakDevice;
    LayoutInflater inflater;

    TopItemView[] layouts ;
    String[] itemTexts = new String[]{"开/关蓝牙","连接蓝牙设备","断开蓝牙设备"};
    int[] itemPic = new int[]{R.mipmap.closebluetooth,R.mipmap.unlink_device,R.mipmap.break_device};

    TextView itemText;
    private ICallbackListener mListener;

    public interface ICallbackListener{
        public void onClick(View view);
    }
    public void setClickListner(ICallbackListener listner){
        mListener = listner;
    }

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }



    /**
     * 初始化布局
     */
    private void initView() {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.bluetooth_link_layout,this);
        findView(view);
        setPressAction();
        initData();
        setOnClick();
    }


    /**
     * 为每个item设置点击事件
     */
    private void setOnClick() {
        keyBluetooth.setOnClickListener(new ItemListener());
        linkDevice.setOnClickListener(new ItemListener());
        breakDevice.setOnClickListener(new ItemListener());
    }
    class ItemListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            if (mListener!= null) {
                mListener.onClick(v);
            }
        }
    }

    /**
     * 为没个item设置一个按压反应，给用户一个操作反馈
     */
    private void setPressAction() {
        for (int i = 0;i < layouts.length;i++){
            layouts[i].setOnTouchListener(new ItemOnTouchListener());
        }

    }
    public void setItem1Selected(){
        keyBluetooth.setTextColor(getResources().getColor(R.color.press_text_color));
        keyBluetooth.setImgBackground(R.mipmap.openbluetooth);
    }
    public void setItem1UnSelected(){
        keyBluetooth.setTextColor(getResources().getColor(R.color.default_text_color));
        keyBluetooth.setImgBackground(R.mipmap.closebluetooth);
    }
    public void setItem2Selected(){
        linkDevice.setTextColor(getResources().getColor(R.color.press_text_color));
       linkDevice.setImgBackground(R.mipmap.link_device);
    }
    public void setItem2UnSelected(){
        linkDevice.setTextColor(getResources().getColor(R.color.default_text_color));
       linkDevice.setImgBackground(R.mipmap.unlink_device);
    }


    class ItemOnTouchListener implements OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TopItemView topItemView = (TopItemView) v;
            switch (v.getId()){
                case R.id.break_device:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            topItemView.setTextColor(getResources().getColor(R.color.press_text_color));
                            topItemView.setImgBackground(R.mipmap.unbreak_device);
                            break;
                        case MotionEvent.ACTION_UP:
                            topItemView.setTextColor(getResources().getColor(R.color.default_text_color));
                            topItemView.setImgBackground(R.mipmap.break_device);
                            break;
                    }
                    break;
            }
            return false;
        }
    }

    /**
     * 初始数据初始化
     */
    private void initData() {
           for (int i = 0;i<layouts.length;i++){
               layouts[i].setText(itemTexts[i]);
               layouts[i].setImgBackground(itemPic[i]);
           }
    }

    /**
     * 找控件
     * @param view
     */
    private void findView(View view) {
        keyBluetooth = (TopItemView) view.findViewById(R.id.key_bluetooth);
        linkDevice = (TopItemView) view.findViewById(R.id.link_device);
        breakDevice = (TopItemView) view.findViewById(R.id.break_device);
        layouts = new TopItemView[]{keyBluetooth,linkDevice,breakDevice};
    }

}
