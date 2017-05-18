package com.devwu.gesturelockviewgroup.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.devwu.gesturelockviewgroup.GestureLockViewGroup;


/**
 * Created by WuNan on 17/5/11.
 */

public abstract class GestureLockNodeView extends View {
    /**
     * GestureLockView的三种状态
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface State {
        int STATE_DEFAULT = 0;     //默认状态
        int STATE_TOUCH_MOVE = 1;  //手势移动中
        int STATE_TOUCH_UP = 2;    //手指抬起
    }

    /**
     * 绘制箭头
     *
     * @param canvas 旋转好的画布
     */
    protected abstract void drawArrow(Canvas canvas, Paint paint, @State int currentState);

    /**
     * onMeasure回调方法
     *
     * @param sideLength 每个圆圈的边长
     */
    protected abstract void doMeasure(int sideLength);

    /**
     * onDraw回调方法，用于绘制圆心圆环及箭头
     *
     * @param canvas        画布
     * @param currentState 当前手指状态
     */
    protected abstract void doDraw(Canvas canvas, Paint paint, @State int currentState);

    private int mState = State.STATE_DEFAULT;
    private Paint mPaint;
    private int mArrowDegree = -1;

    public GestureLockNodeView(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 取长和宽中的较小值为边长
        int sideLength = width < height ? width : height;
        doMeasure(sideLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        doDraw(canvas, mPaint, mState);
        if (GestureLockViewGroup.isDebug) {
            mPaint.setTextSize(50);
            canvas.drawText(getId() + "", getWidth() / 2 - 25, getHeight() / 2 - 25, mPaint);
        }
        if (mState == State.STATE_DEFAULT) {
            hideArrow();
        } else {
            rotateCanvasForArrow(canvas);
        }
    }

    /**
     * 旋转画布，用于绘制箭头
     *
     * @param canvas 原始画布
     */
    private void rotateCanvasForArrow(Canvas canvas) {
        if (mArrowDegree != -1) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.rotate(mArrowDegree, getWidth() / 2, getHeight() / 2);
            drawArrow(canvas, mPaint, mState);
            canvas.restore();
        }
    }

    /**
     * 隐藏箭头
     */
    public void hideArrow() {
        setArrowDegree(-1);
    }

    /**
     * 设置当前模式并重绘界面
     *
     * @param state
     */
    public void setState(@State int state) {
        this.mState = state;
        invalidate();
    }

    /**
     * 设置箭头角度
     *
     * @param degree 角度
     */
    public void setArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }


}
