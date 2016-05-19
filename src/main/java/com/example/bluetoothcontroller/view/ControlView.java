package com.example.bluetoothcontroller.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bluetoothcontroller.R;
import com.example.bluetoothcontroller.activity.MainActivity;
import com.example.bluetoothcontroller.bluetoothcontroller.MyBlueToothController;
import com.example.bluetoothcontroller.thread.ConnectThread;

/**
 * Created by 杨胜 on 2016/4/17.
 */
public class ControlView extends LinearLayout {

    LayoutInflater inflater;
    private Toast mToast;
    ImageView stopView1, stopView2;
    SingleControlView rootView1, rootView2;
    Button PWMButton1,PWMButton2,PWMButton3,PWMButton4,PWMButton5,startControlButton,stopControlButton;
    Button[] PWMButtons;
    //分别是在SINGLE_LEFT模式下的坐标  SINGLE_RIGHT模式下的坐标 ZOMM模式下的第二个坐标
    float x1, y1, x2, y2, x3, y3, x4, y4, x5, y5;

    public String state;
    public static final String FIRST_PRESS_RIGHT = "firstRight";
    public static final String FIRST_PRESS_LEFT = "firstLeft";
    public static final int NONE = 0;
    public static final int SINGLE_LEFT = 1;
    public static final int SINGLE_RIGHT = 2;
    public static final int ZOOM = 3;
    //模式
    public int MODE = NONE;
    //个个控件的高度 和 宽度
    int rootHeight, rootWidth, stopHeight, stopWidth, totalHeight, totalWidth;
    //rootHeight 和 totalHeight的高度距离
    int opHeightDist;
    //第一次进来
    boolean firstIn = true;
    boolean start,connectDevice;
    //左手抬起 右手抬起
    boolean leftUp, rightUp, allUp;
    MyBlueToothController myBlueToothController;

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myBlueToothController = MyBlueToothController.getController();
        initView();
        setClick();
    }
    public void setConnectDevice(boolean connectState){
        connectDevice = connectState;
    }
    /**
     * 为ControlView的滑动给MainActivity实现接口 在activity实现控制逻辑
     */
    public interface ControlScrollListener{
        public void onScroll(String str);
    }
    ControlScrollListener mControlScrollListener;
    public void setControlScrollListener(ControlScrollListener listener){
        mControlScrollListener = listener;
    }


    /**
     * 为ControlView的按钮给MainActivity实现接口 在activity实现控制逻辑
     */
    public interface ControlButtonClickListener{
        public void click(int viewId);
    }
    ControlButtonClickListener mControlButtonClickListener;
    public void setControlButtonClickListener(ControlButtonClickListener listner){
        mControlButtonClickListener = listner;
    }


    /**
     * 为各个Button设置点击事件
     */
    private void setClick() {
        MyButtonListener listener = new MyButtonListener();
        PWMButton1.setOnClickListener(listener);
        PWMButton2.setOnClickListener(listener);
        PWMButton3.setOnClickListener(listener);
        PWMButton4.setOnClickListener(listener);
        PWMButton5.setOnClickListener(listener);
        startControlButton.setOnClickListener(listener);
        stopControlButton.setOnClickListener(listener);
    }
    class MyButtonListener implements OnClickListener{

        @Override
        public void onClick(View v) {
           /* if (!myBlueToothController.getBlueToothStatus()){
                showToast("请打开蓝牙");
                return;
            }*/
            switch (v.getId()){
                case R.id.pwm_button1:
                    setPWMButtonBackground(0);
                    if (start == true){
                        mControlButtonClickListener.click(R.id.pwm_button1);
                    }
                    break;
                case R.id.pwm_button2:
                    setPWMButtonBackground(1);
                    if (start == true){
                        mControlButtonClickListener.click(R.id.pwm_button2);
                    }
                    break;
                case R.id.pwm_button3:
                    setPWMButtonBackground(2);
                    if (start == true){
                        mControlButtonClickListener.click(R.id.pwm_button3);
                    }
                    break;
                case R.id.pwm_button4:
                    setPWMButtonBackground(3);
                    if (start == true){
                        mControlButtonClickListener.click(R.id.pwm_button4);
                    }
                    break;
                case R.id.pwm_button5:
                    setPWMButtonBackground(4);
                    if (start == true){
                        mControlButtonClickListener.click(R.id.pwm_button5);
                    }
                    break;
                case R.id.start_control:
                    if (connectDevice == false){
                        showToast("未连接设备");
                        break;
                    }
                    if (start == false && connectDevice == true){
                        startControlButton.setBackgroundResource(R.drawable.pwm_button_select);
                        setPWMButtonBackground(0);
                        mControlButtonClickListener.click(R.id.start_control);
                        start = true;
                    }
                    break;
                case R.id.stop_control:
                    if (start == true){
                        startControlButton.setBackgroundResource(R.drawable.pwm_button_unselect);
                        mControlButtonClickListener.click(R.id.stop_control);
                        start = false;
                    }
                    break;
            }
        }
    }

    /**
     * 设置点击pwm按钮时所有按钮的颜色
     */
    private void setPWMButtonBackground(int item) {
        for (int i = 0;i < 5;i++){
            if (i == item){
                PWMButtons[i].setBackgroundResource(R.drawable.pwm_button_select);
                continue;
            }
            PWMButtons[i].setBackgroundResource(R.drawable.pwm_button_unselect);
        }
    }

    private void initView() {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.control_layout, this);
        findView(view);
    }

    /**
     * 计算宽 高
     */
    private void calculateHW() {
        rootWidth = rootView1.getWidth();
        rootHeight = rootView1.getHeight();
        stopWidth = stopView1.getWidth();
        stopHeight = stopView1.getHeight();
        totalWidth = getWidth();
        totalHeight = getHeight();
        opHeightDist = totalHeight - rootHeight;
        Log.v("height,width", rootWidth + " " + rootHeight + " " + stopWidth + " " + stopHeight + " " + totalWidth + " " + totalHeight + " " + opHeightDist + "");
    }

    private void findView(View view) {
        rootView1 = (SingleControlView) view.findViewById(R.id.accelerate_view);
        stopView1 = (ImageView) rootView1.findViewById(R.id.stop_img);
        rootView2 = (SingleControlView) view.findViewById(R.id.control_view);
        stopView2 = (ImageView) rootView2.findViewById(R.id.stop_img);
        PWMButton1 = (Button) view.findViewById(R.id.pwm_button1);
        PWMButton2 = (Button) view.findViewById(R.id.pwm_button2);
        PWMButton3 = (Button) view.findViewById(R.id.pwm_button3);
        PWMButton4 = (Button) view.findViewById(R.id.pwm_button4);
        PWMButton5 = (Button) view.findViewById(R.id.pwm_button5);
        PWMButtons = new Button[]{PWMButton1,PWMButton2,PWMButton3,PWMButton4,PWMButton5};
        startControlButton = (Button) view.findViewById(R.id.start_control);
        stopControlButton = (Button) view.findViewById(R.id.stop_control);
    }
    private void showToast(String text) {
        if( mToast == null) {
            mToast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        }
        else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * layout是相对父控件 上一层控件
     * 自定义放控件
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (MODE == SINGLE_LEFT) {
            //在SINGLE_LEFT的情况下对stopView1的超出边框处理
            if (x1 > rootWidth - stopWidth / 2) {
                x1 = rootWidth - stopWidth / 2;
            }
            if (y1 > totalHeight - stopHeight / 2) {
                y1 = totalHeight - stopHeight / 2;
            }
            if (x1 < stopWidth / 2) {
                x1 = stopWidth / 2;
            }
            if (y1 < opHeightDist + stopWidth / 2) {
                y1 = opHeightDist + stopWidth / 2;
            }
            //layout是相对父控件 上一层控件
            stopView1.layout((int) x1 - stopWidth / 2, (int) y1 - opHeightDist - stopHeight / 2, (int) x1 + stopWidth / 2, (int) y1 - opHeightDist + stopHeight / 2);
            if(x1 < rootWidth / 4){
                mControlScrollListener.onScroll("left1");
            }
            if (x1 > rootWidth * 3 / 4){
                mControlScrollListener.onScroll("right1");
            }
            if (y1 < opHeightDist + rootHeight/4){
                mControlScrollListener.onScroll("top1");
            }
            if (y1 > opHeightDist + rootHeight * 3 /4){
                mControlScrollListener.onScroll("bottom1");
            }
            if (rightUp == true) {
                stopView2.layout(rootWidth / 2 - stopWidth / 2, rootHeight / 2 - stopHeight / 2, rootWidth / 2 + stopWidth / 2, rootHeight / 2 + stopHeight / 2);
            }
        } else if (MODE == SINGLE_RIGHT) {

            //在SINGLE_RIGHT的情况下对stopView2的超出边框处理
            if (x2 > totalWidth - stopWidth / 2) {
                x2 = totalWidth - stopWidth / 2;
            }
            if (y2 > totalHeight - stopHeight / 2) {
                y2 = totalHeight - stopHeight / 2;
            }
            if (x2 < totalWidth - rootWidth + stopWidth / 2) {
                x2 = totalWidth - rootWidth + stopWidth / 2;
            }
            if (y2 < opHeightDist + stopWidth / 2) {
                y2 = opHeightDist + stopWidth / 2;
            }
            stopView2.layout((int) x2 - (totalWidth - rootWidth) - stopWidth / 2, (int) y2 - opHeightDist - stopHeight / 2, (int) x2 - (totalWidth - rootWidth) + stopWidth / 2, (int) y2 - opHeightDist + stopHeight / 2);
            if(x2 < totalWidth - rootWidth * 3 / 4){
                mControlScrollListener.onScroll("left2");
            }
            if (x2 > totalWidth - rootWidth / 4){
                mControlScrollListener.onScroll("right2");
            }
            if (y2 < opHeightDist + rootHeight/4){
                mControlScrollListener.onScroll("top2");
            }
            if (y2 > opHeightDist + rootHeight * 3 /4){
                mControlScrollListener.onScroll("bottom2");
            }
            if (leftUp == true) {
                stopView1.layout(rootWidth / 2 - stopWidth / 2, rootHeight / 2 - stopHeight / 2, rootWidth / 2 + stopWidth / 2, rootHeight / 2 + stopHeight / 2);
            }
        } else if (MODE == ZOOM) {
            if (x3 < x4) {
                //左边按钮第一个按
                //在ZOOM的情况下对stopView1的超出边框处理
                if (x3 > rootWidth - stopWidth / 2) {
                    x3 = rootWidth - stopWidth / 2;
                }
                if (y3 > totalHeight - stopHeight / 2) {
                    y3 = totalHeight - stopHeight / 2;
                }
                if (x3 < stopWidth / 2) {
                    x3 = stopWidth / 2;
                }
                if (y3 < opHeightDist + stopWidth / 2) {
                    y3 = opHeightDist + stopWidth / 2;
                }

                //在ZOOM的情况下对stopView2的超出边框处理
                if (x4 > totalWidth - stopWidth / 2) {
                    x4 = totalWidth - stopWidth / 2;
                }
                if (y4 > totalHeight - stopHeight / 2) {
                    y4 = totalHeight - stopHeight / 2;
                }
                if (x4 < totalWidth - rootWidth + stopWidth / 2) {
                    x4 = totalWidth - rootWidth + stopWidth / 2;
                }
                if (y4 < opHeightDist + stopWidth / 2) {
                    y4 = opHeightDist + stopWidth / 2;
                }
                stopView1.layout((int) x3 - stopWidth / 2, (int) y3 - opHeightDist - stopHeight / 2, (int) x3 + stopWidth / 2, (int) y3 - opHeightDist + stopHeight / 2);
                stopView2.layout((int) x4 - (totalWidth - rootWidth) - stopWidth / 2, (int) y4 - opHeightDist - stopHeight / 2, (int) x4 - (totalWidth - rootWidth) + stopWidth / 2, (int) y4 - opHeightDist + stopHeight / 2);
                if(x3 < rootWidth / 4){
                    mControlScrollListener.onScroll("left1");
                }
                if (x3 > rootWidth * 3 / 4){
                    mControlScrollListener.onScroll("right1");
                }
                if (y3 < opHeightDist + rootHeight/4){
                    mControlScrollListener.onScroll("top1");
                }
                if (y3 > opHeightDist + rootHeight * 3 /4){
                    mControlScrollListener.onScroll("bottom1");
                }


                if(x4 < totalWidth - rootWidth * 3 / 4){
                    mControlScrollListener.onScroll("left2");
                }
                if (x4 > totalWidth - rootWidth / 4){
                    mControlScrollListener.onScroll("right2");
                }
                if (y4 < opHeightDist + rootHeight/4){
                    mControlScrollListener.onScroll("top2");
                }
                if (y4 > opHeightDist + rootHeight * 3 /4){
                    mControlScrollListener.onScroll("bottom2");
                }

            } else if (x3 > x4) {
                //右边按钮第一个按
                //在ZOOM的情况下对stopView2的超出边框处理
                if (x3 > totalWidth - stopWidth / 2) {
                    x3 = totalWidth - stopWidth / 2;
                }
                if (y3 > totalHeight - stopHeight / 2) {
                    y3 = totalHeight - stopHeight / 2;
                }
                if (x3 < totalWidth - rootWidth + stopWidth / 2) {
                    x3 = totalWidth - rootWidth + stopWidth / 2;
                }
                if (y3 < opHeightDist + stopWidth / 2) {
                    y3 = opHeightDist + stopWidth / 2;
                }
                //在ZOOM的情况下对stopView1的超出边框处理
                if (x4 > rootWidth - stopWidth / 2) {
                    x4 = rootWidth - stopWidth / 2;
                }
                if (y4 > totalHeight - stopHeight / 2) {
                    y4 = totalHeight - stopHeight / 2;
                }
                if (x4 < stopWidth / 2) {
                    x4 = stopWidth / 2;
                }
                if (y4 < opHeightDist + stopWidth / 2) {
                    y4 = opHeightDist + stopWidth / 2;
                }
                stopView1.layout((int) x4 - stopWidth / 2, (int) y4 - opHeightDist - stopHeight / 2, (int) x4 + stopWidth / 2, (int) y4 - opHeightDist + stopHeight / 2);
                stopView2.layout((int) x3 - (totalWidth - rootWidth) - stopWidth / 2, (int) y3 - opHeightDist - stopHeight / 2, (int) x3 - (totalWidth - rootWidth) + stopWidth / 2, (int) y3 - opHeightDist + stopHeight / 2);
                if(x4 < rootWidth / 4){
                    mControlScrollListener.onScroll("left1");
                }
                if (x4 > rootWidth * 3 / 4){
                    mControlScrollListener.onScroll("right1");
                }
                if (y4 < opHeightDist + rootHeight/4){
                    mControlScrollListener.onScroll("top1");
                }
                if (y4 > opHeightDist + rootHeight * 3 /4){
                    mControlScrollListener.onScroll("bottom1");
                }


                if(x3 < totalWidth - rootWidth * 3 / 4){
                    mControlScrollListener.onScroll("left2");
                }
                if (x3 > totalWidth - rootWidth / 4){
                    mControlScrollListener.onScroll("right2");
                }
                if (y3 < opHeightDist + rootHeight/4){
                    mControlScrollListener.onScroll("top2");
                }
                if (y3 > opHeightDist + rootHeight * 3 /4){
                    mControlScrollListener.onScroll("bottom2");
                }
            }
        } else {
            if (allUp == true) {
                stopView1.layout(rootWidth / 2 - stopWidth / 2, rootHeight / 2 - stopHeight / 2, rootWidth / 2 + stopWidth / 2, rootHeight / 2 + stopHeight / 2);
                stopView2.layout(rootWidth / 2 - stopWidth / 2, rootHeight / 2 - stopHeight / 2, rootWidth / 2 + stopWidth / 2, rootHeight / 2 + stopHeight / 2);
            }
            super.onLayout(changed, l, t, r, b);
        }
        if (firstIn) {
            firstIn = false;
            calculateHW();
        }
    }

    /**
     * 多点触碰处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.v("pressDown",event.getX(0)+"");
                allUp = false;
                if (event.getX(0) < rootWidth && event.getY(0) > totalHeight - rootHeight) {
                    state = FIRST_PRESS_LEFT;
                    x1 = event.getX(0);
                    y1 = event.getY(0);
                    MODE = SINGLE_LEFT;
                } else if (event.getX(0) > totalWidth - rootWidth && event.getY(0) > totalHeight - rootHeight) {
                    state = FIRST_PRESS_RIGHT;
                    x2 = event.getX(0);
                    y2 = event.getY(0);
                    MODE = SINGLE_RIGHT;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (MODE == SINGLE_LEFT) {
                    x1 = event.getX(0);
                    y1 = event.getY(0);
                } else if (MODE == SINGLE_RIGHT) {
                    x2 = event.getX(0);
                    y2 = event.getY(0);
                } else if (MODE == ZOOM) {
                    x3 = event.getX(0);
                    y3 = event.getY(0);
                    x4 = event.getX(1);
                    y4 = event.getY(1);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v("pressDown",event.getX(1)+"");
                x3 = event.getX(0);
                y3 = event.getY(0);
                x4 = event.getX(1);
                y4 = event.getY(1);
                Log.v("pointerDown", "x3=" + x3 + " x4=" + x4);
                if (state == FIRST_PRESS_RIGHT && rightUp == true) {
                    if (x3 > totalWidth - rootWidth && y3 > totalHeight - rootHeight) {
                        MODE = ZOOM;
                        rightUp = false;
                    }
                    break;
                }
                if (state == FIRST_PRESS_LEFT && leftUp == true) {
                    if (x3 < rootWidth && y3 > totalHeight - rootHeight) {
                        MODE = ZOOM;
                        leftUp = false;
                    }
                    break;
                }
                if (MODE == SINGLE_RIGHT) {
                    if (x4 < rootWidth && y4 > totalHeight - rootHeight) {
                        MODE = ZOOM;
                    }
                } else if (MODE == SINGLE_LEFT) {
                    if (x4 > totalWidth - rootWidth && y4 > totalHeight - rootHeight) {
                        MODE = ZOOM;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                MODE = NONE;
                allUp = true;
                state = null;
                leftUp = false;
                rightUp = false;
                mControlScrollListener.onScroll("stop");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //此时的event.getActionIndex()是第几个触摸点松开
                if (state == FIRST_PRESS_LEFT) {
                    Log.v("ActinIndex", event.getActionIndex() + "");
                    MODE = event.getActionIndex() == 0 ? SINGLE_RIGHT : SINGLE_LEFT;
                    if (MODE == SINGLE_RIGHT) {
                        leftUp = true;
                    } else if (MODE == SINGLE_LEFT) {
                        rightUp = true;
                    }
                } else if (state == FIRST_PRESS_RIGHT) {
                    MODE = event.getActionIndex() == 0 ? SINGLE_LEFT : SINGLE_RIGHT;
                    Log.v("ActinIndex", event.getActionIndex() + "");
                    if (MODE == SINGLE_LEFT) {
                        rightUp = true;
                    } else if (MODE == SINGLE_RIGHT) {
                        leftUp = true;
                    }
                }
                break;
        }
        requestLayout();
        return true;
    }
}
