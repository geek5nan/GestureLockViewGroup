package com.devwu.common.utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by WuNan-QF on 16/7/29.
 * ViewUtil
 */
public class ViewUtil {
    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static void toggleVisibility(View view) {
        view.setVisibility(isVisible(view) ? View.GONE : View.VISIBLE);
    }

    public static void setVisible(View view,boolean visible){
        view.setVisibility(visible?View.VISIBLE:View.GONE);
    }

    public static void show(View view) {
        setVisible(view,true);
    }

    public static void hide(View view) {
        setVisible(view,false);
    }


    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    public static int getCenterX(View view) {
        return (view.getLeft() + view.getRight()) / 2;
    }

    public static int getCenterY(View view) {
        return (view.getTop() + view.getBottom()) / 2;
    }

    public static Point getCenter(View view) {
        return new Point(getCenterX(view), getCenterY(view));
    }
}
