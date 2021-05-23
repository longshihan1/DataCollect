package com.longshihan.collect.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.longshihan.collect.R;
import com.longshihan.collect.apm.fps.ChoreographerHelp;
import com.longshihan.collect.apm.fps.listener.FpsObserver;
import com.longshihan.collect.init.TraceManager;
import com.longshihan.collect.init.Utils;
import com.longshihan.collect.utils.DensityUtil;

@SuppressLint("AppCompatCustomView")
public class FloatView extends TextView {
    boolean showBg;
    private int lastX;
    private int lastY;
    private int screenHeight;
    private int screenWidth;
    //WindowManager.LayoutParams 的x坐标
    private float lp_x;

    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams params;

    public FloatView(Context context) {
        super(context);
        params = new WindowManager.LayoutParams(DensityUtil.INSTANCE.px2dip(TraceManager.mContext, 20)
                , DensityUtil.INSTANCE.px2dip(TraceManager.mContext, 20));
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        screenHeight = Utils.getScreenHeight(getContext());
        screenWidth = Utils.getScreenWidth(getContext());
        ChoreographerHelp.INSTANCE.addObserver(new FpsObserver() {
            @Override
            public void frameCallback(int count) {
                super.frameCallback(count);
                setTextStr(count+"");
            }
        });
    }
    private int startX, startY;

    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的X，Y
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startX = lastX = rawX;
                startY = lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算手指移动的距离
                float x = rawX - lastX;
                float y = rawY - lastY;
                if (getLayoutParams() instanceof WindowManager.LayoutParams) {
                    params = (WindowManager.LayoutParams) getLayoutParams();
                    //将移动距离累加到Lp中
                    params.x += (int) x;
                    params.y += (int) y;
                    //将Lp设置给DragTableButton
                    wm.updateViewLayout(this, params);
                }
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int dx = (int) event.getRawX() - startX;
                int dy = (int) event.getRawY() - startY;
                if (Math.abs(dx) < 3 && Math.abs(dy) < 3) {
                    performClick();//重点，确保DragFloatActionButton2.setOnclickListener生效
                    break;
                }
                if (rawX >= screenWidth / 2) {
                    //靠右吸附
                    ObjectAnimator oa = ObjectAnimator.ofFloat(this, "lp_x", params.x, screenWidth - getWidth());
                    oa.setInterpolator(new DecelerateInterpolator());
                    oa.setDuration(500);
                    oa.start();
                } else {
                    //靠左吸附
                    ObjectAnimator oa = ObjectAnimator.ofFloat(this, "lp_x", params.x, 0);
                    oa.setInterpolator(new DecelerateInterpolator());
                    oa.setDuration(500);
                    oa.start();
                }
                break;
        }
        return true;
    }


    public float getLp_x() {
        return lp_x;
    }

    public void setLp_x(float lp_x) {
        this.lp_x = lp_x;
        params.x = (int) lp_x;
        wm.updateViewLayout(this,params);
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