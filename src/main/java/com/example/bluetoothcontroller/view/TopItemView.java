package com.example.bluetoothcontroller.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bluetoothcontroller.R;

/**
 * Created by 杨胜 on 2016/4/25.
 */
public class TopItemView extends LinearLayout {

    private TextView mTopTextView;
    private ImageView mTopImageView;
    public TopItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bluetooth_link_layout_item,this);
        findView(view);
    }

    private void findView(View view) {
        mTopImageView = (ImageView) view.findViewById(R.id.top_img);
        mTopTextView = (TextView) view.findViewById(R.id.top_txt);
    }
    public void setText(String string){
        mTopTextView.setText(string);
    }
    public void setTextColor(@ColorInt int colorId){
        mTopTextView.setTextColor(colorId);
    }
    public void setImgBackground(@DrawableRes int mipmapId){
        mTopImageView.setImageResource(mipmapId);
    }
}
