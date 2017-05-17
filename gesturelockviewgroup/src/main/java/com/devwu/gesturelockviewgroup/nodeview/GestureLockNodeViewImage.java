package com.devwu.gesturelockviewgroup.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import com.devwu.gesturelockviewgroup.GestureLockViewGroup;


/**
 * Created by WuNan on 17/5/15.
 * 图片LockView
 */

public class GestureLockNodeViewImage extends GestureLockNodeView {
    /**
     * 两个图片，可由用户自定义，初始化时由GestureLockViewGroup传入
     */
    private Drawable mDrawableBg;
    private Drawable mDrawableArrow;

    public static int[] stateDefault = new int[]{-android.R.attr.state_pressed, -android.R.attr.state_selected};
    public static int[] stateMoving = new int[]{android.R.attr.state_pressed, -android.R.attr.state_selected};
    public static int[] stateIncorrect = new int[]{-android.R.attr.state_pressed, android.R.attr.state_selected};
    public static int[] stateCorrect = new int[]{android.R.attr.state_pressed, android.R.attr.state_selected};

    /**
     * 工厂方法
     */
    public static GestureLockNodeViewImage create(Context context, @DrawableRes int drawableBg, @DrawableRes int drawableArrow) {
        return new GestureLockNodeViewImage(context, context.getResources().getDrawable(drawableBg), context.getResources().getDrawable(drawableArrow));
    }

    public static GestureLockNodeViewImage create(Context context, Drawable drawableBg, Drawable drawableArrow) {
        return new GestureLockNodeViewImage(context, drawableBg, drawableArrow);
    }

    private GestureLockNodeViewImage(Context context, Drawable drawableBg, Drawable drawableArrow) {
        super(context);
        setSelected(false);
        setPressed(false);
        mDrawableBg = drawableBg;
        mDrawableArrow = drawableArrow;
        setBackgroundDrawable(mDrawableBg);
        mDrawableBg.setCallback(this);
        mDrawableArrow.setCallback(this);
    }

    @Override
    protected void doMeasure(int sideLength) {
        //设箭头边长为lockBg的1/4
        int sideLengthOfArrow = sideLength >> 2;
        //将箭头水平居中放置
        int x = (sideLength >> 1) - (sideLengthOfArrow >> 1);
        mDrawableArrow.setBounds(x, 0, x + sideLengthOfArrow, sideLengthOfArrow);

    }

    @Override
    protected void doDraw(Canvas canvas, Paint paint, @Statu int currentStatus) {
        mDrawableBg.setState(getDrawableStatus(currentStatus));
    }

    @Override
    protected void drawArrow(Canvas canvas, Paint paint, @Statu int currentStatus) {
        mDrawableArrow.draw(canvas);
        mDrawableArrow.setState(getDrawableStatus(currentStatus));
    }

    private int[] getDrawableStatus(@Statu int currentStatus) {
        int drawableStates[] = stateDefault;
        switch (currentStatus) {
            case Statu.STATU_DEFAULT: {
                drawableStates = stateDefault;
            }
            break;
            case Statu.STATU_TOUCH_MOVE: {
                drawableStates = stateMoving;
            }
            break;
            case Statu.STATU_TOUCH_UP: {
                drawableStates = GestureLockViewGroup.isCorrect ? stateCorrect : stateIncorrect;
            }
            break;
        }
        return drawableStates;
    }
}
