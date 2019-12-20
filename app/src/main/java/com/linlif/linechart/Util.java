package com.linlif.linechart;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by linlif on 2019-12-20
 */
public class Util {

    private static int sScreenWidth = -1;

    /**
     * 获取设备屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {

        if (sScreenWidth > 0) {
            return sScreenWidth;
        }

        DisplayMetrics displayMetrics = getDisplayMetrics(App.getContext());
        int screenWidth = displayMetrics != null ? displayMetrics.widthPixels : DensityUtil.dip2px(360f);
        int srceenHeight = displayMetrics != null ? displayMetrics.heightPixels : DensityUtil.dip2px(640f);

        /**
         *  针对获取到屏幕宽度大于屏幕高度情况下：
         *    尝试解决屏幕因某种情况下获取屏幕宽高信息交换
         *  ，导致APP样式展示错乱问题，因此需要width与height的值进行替换处理
         */
        if (screenWidth > srceenHeight) {

            screenWidth = srceenHeight;
        }

        sScreenWidth = screenWidth;
        // 1. 优先走自定义方案获取 DisplayMetrics对象，否则使用 640dp
        return screenWidth;
    }


    /***
     *   获取DisplayMetrics对象
     *
     * @param context
     * @return
     */
    private static DisplayMetrics getDisplayMetrics(Context context) {

        if (context == null) {

            return null;
        }

        DisplayMetrics screenDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        try {

            // Get the real display metrics if we are using API level 17 or higher.
            // The real metrics include system decor elements (e.g. soft menu bar).
            //
            // See: http://developer.android.com/reference/android/view/Display.html#getRealMetrics(android.util.DisplayMetrics)
            if (Build.VERSION.SDK_INT >= 17) {

                display.getRealMetrics(screenDisplayMetrics);
            } else {
                // For 14 <= API level <= 16, we need to invoke getRawHeight and getRawWidth to get the real dimensions.
                //
                // Reflection exceptions are rethrown at runtime.
                //
                // See: http://stackoverflow.com/questions/14341041/how-to-get-real-screen-height-and-width/23861333#23861333

                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                screenDisplayMetrics.widthPixels = (Integer) mGetRawW.invoke(display);
                screenDisplayMetrics.heightPixels = (Integer) mGetRawH.invoke(display);
            }

        } catch (Throwable e) {

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            screenDisplayMetrics.setTo(displayMetrics);
        }
        return screenDisplayMetrics;
    }

}
