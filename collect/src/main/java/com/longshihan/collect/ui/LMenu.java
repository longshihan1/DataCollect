package com.longshihan.collect.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.longshihan.collect.R;

/**
 * @author longshihan
 * @time 2020/7/26
 */
public class LMenu extends LinearLayout {
    private View vMenu;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private int touchSlop;
    private int showY;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;

    public LMenu(Context context) {
        super(context);
    }

    public LMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LMenu(final Context context, int y) {
        super(context);
        inflate(context, R.layout.l_menu_layout, this);
        setGravity(Gravity.CENTER_VERTICAL);
        this.showY = y;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        vMenu = findViewById(R.id.menu);
        vMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SettingActivity.class));
            }
        });
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
        params.y = showY;
        return params;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = (int) (event.getRawY()-25);   //25是系统状态栏的高度
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX =  event.getX();
                mTouchStartY =  event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX=mTouchStartY=0;
                break;
        }
        return true;
    }

    private void updateViewPosition(){
        //更新浮动窗口位置参数
        params.x=(int)( x-mTouchStartX);
        params.y=(int) (y-mTouchStartY);
        windowManager.updateViewLayout(this, params);

    }


    public void show() {
        try {
            windowManager.addView(this, getWindowLayoutParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            windowManager.removeView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
