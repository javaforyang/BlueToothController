package com.example.bluetoothcontroller.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bluetoothcontroller.R;

/**
 * Created by 杨胜 on 2016/4/16.
 */
public class SingleControlView extends LinearLayout {

    ImageView stopImg;
    LayoutInflater inflater;
    private float downX,downY;
    private float lastX,lastY;
    private float moveDistX,moveDistY;
    private boolean canTouch = false;

    public SingleControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.single_control_layout,this);
      //  findView(view);

    }

    /**
     * 寻找控件
     * @param view
     */
    /*private void findView(View view) {
        //stopImg = (ImageView) view.findViewById(R.id.stop_img);
    }*/

   /* @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (canTouch) {
            Log.v("layout", "" + getChildCount());
            ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(0);
            stopImg = (ImageView) viewGroup.getChildAt(0);
            Log.v("size", "width=" + getWidth() + " height=" + getHeight());
            if (downX > getWidth() -stopImg.getWidth()/2){
                downX = getWidth()-stopImg.getWidth()/2;
            }
            if (downX <  stopImg.getWidth()/2){
                downX = stopImg.getWidth()/2;
            }
            if (downY < stopImg.getHeight()/2){
                downY = stopImg.getHeight()/2;
            }
            if (downY > getHeight() - stopImg.getHeight()/2){
                downY = getHeight() - stopImg.getHeight()/2;
            }
            stopImg.layout((int)downX - stopImg.getWidth()/2,(int)downY - stopImg.getHeight()/2,(int)downX + stopImg.getWidth()/2,(int)downY+ stopImg.getHeight()/2);
        }else
        super.onLayout(changed, l, t, r, b);
    }*/

    /**
     *
     * getX取得是相对父控件的坐标 getRawX是相对左上角的坐标
     * @param event
     * @return
     */
    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                canTouch = true;
                Log.v("111", "onTouchEvent: down");
                //开始向设备发送数据
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v("111", "onTouchEvent: move");
                //根据x,y的值向设备发送数据
                downX = event.getX();
                downY = event.getY();
                moveDistX += downX - lastX;
                moveDistY += downY - lastY;
                break;
            case MotionEvent.ACTION_UP:
                canTouch = false;
                Log.v("111", "onTouchEvent: up");
                //发送停止指令

                break;
        }
        Log.v("local ", "downX=" + downX + "downY=" + downY );
        lastX = downX;
        lastY = downY;
        requestLayout();
        return true;//true代表此层消费
    }*/
}
