package com.longshihan.collect.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.longshihan.collect.R;
import com.longshihan.collect.apm.fps.ChoreographerHelp;
import com.longshihan.collect.apm.fps.listener.FpsObserver;
import com.longshihan.collect.init.TraceManager;
import com.longshihan.collect.utils.DensityUtil;

@SuppressLint("AppCompatCustomView")
public class FloatView extends TextView {
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    boolean showBg;

    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams params;

    public FloatView(Context context) {
        super(context);
        params = new WindowManager.LayoutParams(DensityUtil.INSTANCE.px2dip(TraceManager.mContext, 20)
                , DensityUtil.INSTANCE.px2dip(TraceManager.mContext, 20));
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        ChoreographerHelp.INSTANCE.addObserver(new FpsObserver() {
            @Override
            public void frameCallback(int count) {
                super.frameCallback(count);
                setTextStr(count+"");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - 25;   //25是系统状态栏的高度

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return true;
    }

    public void setTextStr(String value){
        if (!showBg) {
            setText(value);
        }
    }

    public void configText(boolean showBg){
        if (showBg){
            setBackgroundResource(R.drawable.ltransformmenu);
        }else {
            setBackground(null);
        }
    }

    private void updateViewPosition() {
        //更新浮动窗口位置参数
        params.x = (int) (x - mTouchStartX);
        params.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(this, params);

    }

    public void show() {
        try {
            wm.addView(this, getWindowLayoutParams());
            setBackgroundResource(R.drawable.ltransformmenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WindowManager.LayoutParams getWindowLayoutParams() {
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 10;
        params.y = 10;
        return params;
    }

    public void dismiss() {
        try {
            wm.removeView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}