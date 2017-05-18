package com.devwu.gesturelockviewgroup.nodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.devwu.gesturelockviewgroup.GestureLockViewGroup;


/**
 * Created by WuNan on 17/5/15.
 * 绘制LockView
 */
public class GestureLockNodeViewDraw extends GestureLockNodeView {

    /**
     * 外圆的半径
     */
    private int mRadius;
    /**
     * 外圆线宽度
     */
    private int mStrokeWidth = 2;
    /**
     * 内圆的半径 = mInnerCircleRadiusRate * mRadus
     */
    private float mInnerCircleRadiusRate = 0.3F;
    /**
     * 箭头（小三角最长边的一半长度 = mArrawRate * mWidth / 2 ）
     */
    private float mArrowRate = 0.333f;
    private Path mArrowPath;
    /**
     * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
     */
    private int mColorDefault;
    private int mColorMoving;
    private int mColorCorrect;
    private int mColorIncorrect;

    public GestureLockNodeViewDraw(Context context, int colorDefault, int colorMoving, int colorCorrect, int colorIncorrect) {
        super(context);
        this.mColorDefault = colorDefault;
        this.mColorMoving = colorMoving;
        this.mColorCorrect = colorCorrect;
        this.mColorIncorrect = colorIncorrect;
        mArrowPath = new Path();
    }

    @Override
    protected void doMeasure(int sideLength) {
        mRadius = sideLength / 2;
        mRadius -= mStrokeWidth / 2;
        prepareArrow(sideLength);

    }


    @Override
    protected void doDraw(Canvas canvas, Paint paint, @State int currentState) {
        // 设置画笔颜色
        paint.setColor(getPaintColor(currentState));
        // 绘制外圆
        paint.setStyle(Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, paint);
        // 绘制内圆芯
        paint.setStyle(Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius * mInnerCircleRadiusRate, paint);
    }

    /**
     * 根据边长制作形为等腰三角形的箭头
     *
     * @param sideLength 边长
     */
    private void prepareArrow(int sideLength) {
        // 制作三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        float mArrowLength = sideLength / 2 * mArrowRate;
        mArrowPath.moveTo(sideLength / 2, mStrokeWidth + 2);
        mArrowPath.lineTo(sideLength / 2 - mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.lineTo(sideLength / 2 + mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);
    }

    /**
     * 获取画笔颜色
     *
     * @param currentStatus
     * @return 画笔颜色
     */
    private int getPaintColor(@State int currentStatus) {
        int paintColor = mColorDefault;
        switch (currentStatus) {
            case State.STATE_TOUCH_MOVE:
                paintColor = mColorMoving;
                break;
            case State.STATE_TOUCH_UP:
                paintColor = GestureLockViewGroup.isCorrect ? mColorCorrect : mColorIncorrect;
                break;
            case State.STATE_DEFAULT:
                paintColor = mColorDefault;
                break;
        }
        return paintColor;
    }


    /**
     * 将箭头绘制到旋转好的画布上
     *
     * @param canvas 旋转好的画布
     */
    @Override
    protected void drawArrow(Canvas canvas, Paint paint,@State int currentState) {
        canvas.drawPath(mArrowPath, paint);
    }
}
