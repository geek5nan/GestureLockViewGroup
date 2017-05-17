package com.devwu.gesturelockviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.devwu.common.utils.AppUtils;
import com.devwu.common.utils.ViewUtil;
import com.devwu.gesturelockviewgroup.listener.SettingListener;
import com.devwu.gesturelockviewgroup.listener.VerifyListener;
import com.devwu.gesturelockviewgroup.nodeview.GestureLockNodeView;
import com.devwu.gesturelockviewgroup.provider.nodeview.NodeViewProvider;
import com.devwu.gesturelockviewgroup.provider.nodeview.NodeViewProviderDraw;
import com.devwu.gesturelockviewgroup.provider.password.PasswordProvider;
import com.devwu.gesturelockviewgroup.provider.password.PasswordProviderDefault;

import java.util.ArrayList;
import java.util.List;


/**
 * 手势锁ViewGroup
 */
public class GestureLockViewGroup extends RelativeLayout {

    private static final String TAG = "GestureLockViewGroup";
    /**
     * 保存所有的GestureLockNodeView
     */
    private GestureLockNodeView[] mGestureLockNodeViews;
    /**
     * 每行NodeView的个数,默认为3
     */
    private int mCount = 3;
    /**
     * 保存用户选中的NodeView的id
     */
    private List<Integer> mChoose = new ArrayList<>();
    private String mFirstInputPassword = "";
    private String mCurrentChooseString = "";
    /**
     * NodeView无手指触摸的状态下圆的颜色
     */
    public int mColorDefault = 0xFF858585;
    /**
     * NodeView手指触摸的状态下圆的颜色
     */
    public int mColorMoving = 0XFF00A6EF;
    /**
     * NodeView手指抬起的状态下,正确时圆的颜色
     */
    public int mColorCorrect = 0xFF91DC5A;
    /**
     * NodeView手指抬起的状态下，错误圆的颜色
     */
    public int mColorIncorrect = 0xFFFF0000;
    /**
     * GestureLockViewGroup 最小边的长度
     */
    private int mMinSideLength = 0;
    /**
     * 指引线宽度
     */
    private int mLineWidth = 0;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 指引线路径
     */
    private Path mPath;
    /**
     * 指引线的开始位置x
     */
    private Point mLastPoint = new Point();
    /**
     * 指引下的结束位置
     */
    private Point mCurrentPoint = new Point();
    /**
     * 最大尝试次数
     */
    private int mTryTimes = 0;
    /**
     * 输入的手势密码是否正确
     */
    public static boolean isCorrect = false;
    /**
     * 调试模式，开启时可显示NodeView的标志
     */
    public static boolean isDebug = false;
    /**
     * 回调接口
     */
    private SettingListener mSettingListener;
    private VerifyListener mVerifyListener;
    private NodeViewProvider mNodeViewProvider;
    private static PasswordProvider sPasswordProvider;


    public GestureLockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 获得所有自定义的参数的值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GestureLockViewGroup, defStyle, 0);
        mColorDefault = a.getColor(R.styleable.GestureLockViewGroup_color_default, mColorDefault);
        mColorMoving = a.getColor(R.styleable.GestureLockViewGroup_color_moving, mColorMoving);
        mColorCorrect = a.getColor(R.styleable.GestureLockViewGroup_color_correct, mColorCorrect);
        mColorIncorrect = a.getColor(R.styleable.GestureLockViewGroup_color_incorrect, mColorIncorrect);
        mLineWidth = a.getInt(R.styleable.GestureLockViewGroup_line_width, mLineWidth);
        mCount = a.getInt(R.styleable.GestureLockViewGroup_count, mCount);
        a.recycle();
        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMinSideLength = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        initViews();
    }

    private void initViews() {
        // 初始化mGestureLockViews
        if (mGestureLockNodeViews == null) {
            /**
             * 设每个NodeView的宽度为2x,NodeView的间隔为x.若宽度为W,则x = W/(2x+x+1)
             */
            double singleX = mMinSideLength / (2 * mCount + mCount + 1);
            // NodeView的间距
            int mMarginBetweenNodeView = (int) singleX;
            // NodeView的宽度
            int mNodeViewWidth = (int) singleX * 2;
            // 设置画笔的默认宽度为NodeViewWidth*0.02
            mPaint.setStrokeWidth(mLineWidth == 0 ? mNodeViewWidth * 0.02f : mLineWidth);

            mGestureLockNodeViews = new GestureLockNodeView[mCount * mCount];
            for (int i = 0; i < mGestureLockNodeViews.length; i++) {
                //初始化每个GestureLockView
                if (mNodeViewProvider == null) {
                    mNodeViewProvider = new NodeViewProviderDraw.Builder(getContext()).setColorDefault(mColorDefault).setColorMoving(mColorMoving).setColorIncorrect(mColorIncorrect).setColorCorrect(mColorCorrect).build();
                }
                mGestureLockNodeViews[i] = mNodeViewProvider.initChildView();
                mGestureLockNodeViews[i].setId(i + 1);
                //设置参数，主要是定位GestureLockView间的位置
                LayoutParams lockerParams = new LayoutParams(mNodeViewWidth, mNodeViewWidth);

                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF, mGestureLockNodeViews[i - 1].getId());
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    lockerParams.addRule(RelativeLayout.BELOW, mGestureLockNodeViews[i - mCount].getId());
                }
                //设置右下左上的边距
                int rightMargin = mMarginBetweenNodeView;
                int bottomMargin = mMarginBetweenNodeView;
                int leftMargin = 0;
                int topMargin = 0;
                //每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
                if (i >= 0 && i < mCount)// 第一行
                {
                    topMargin = mMarginBetweenNodeView;
                }
                if (i % mCount == 0)// 第一列
                {
                    leftMargin = mMarginBetweenNodeView;
                }

                lockerParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                addView(mGestureLockNodeViews[i], lockerParams);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                reset();     // 重置
                break;
            case MotionEvent.ACTION_MOVE: {
                drawAndGetSelectedWhenTouchMove(x, y);
                drawArrow(x, y);
                break;
            }
            case MotionEvent.ACTION_UP:
                if (TextUtils.isEmpty(sPasswordProvider.getPassword())) {
                    if (mSettingListener != null)
                        settingPassword();  //设置密码
                } else {
                    if (mVerifyListener != null) {
                        isCorrect = sPasswordProvider.getPassword().equals(mCurrentChooseString);
                        //输入正确时恢复重试次数
                        mTryTimes = isCorrect ? 0 : mTryTimes + 1;
                        mVerifyListener.onGestureVerify(isCorrect, this.mTryTimes);  //将结果回调
                    }
                }
                drawWhenTouchUp();
                break;
        }
        invalidate();
        return true;
    }

    private void drawAndGetSelectedWhenTouchMove(int x, int y) {
        mPaint.setColor(mColorMoving);
        mPaint.setAlpha(50);
        GestureLockNodeView child = getChildIdByPos(x, y);
        if (child != null) {
            int cId = child.getId();
            if (!mChoose.contains(cId)) {
                mChoose.add(cId);
                mCurrentChooseString = mCurrentChooseString + cId;
                child.setMode(GestureLockNodeView.Statu.STATU_TOUCH_MOVE);
                // 设置指引线的起点
                mLastPoint.x = ViewUtil.getCenterX(child);
                mLastPoint.y = ViewUtil.getCenterY(child);
                // 当前添加为第一个,设置指引线的起点为NodeView的中心
                if (mChoose.size() == 1) {
                    mPath.moveTo(mLastPoint.x, mLastPoint.y);
                } else
                // 非第一个，将上次和本次选中的NodeView用指引线连接
                {
                    mPath.lineTo(mLastPoint.x, mLastPoint.y);
                }
            }
        }
        // 指引线的终点
        mCurrentPoint.x = x;
        mCurrentPoint.y = y;
    }

    private void drawArrow(int x, int y) {
        for (int i = 0; i < mChoose.size(); i++) {
            GestureLockNodeView startChild = (GestureLockNodeView) findViewById(mChoose.get(i));
            int angle;
            if (i == mChoose.size() - 1) {
                double dx = x - ViewUtil.getCenterX(startChild);
                double dy = y - ViewUtil.getCenterY(startChild);
                angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
            } else {
                GestureLockNodeView nextChild = (GestureLockNodeView) findViewById(mChoose.get(i + 1));
                double dx = nextChild.getX() - startChild.getX();
                double dy = nextChild.getY() - startChild.getY();
                // 计算角度
                angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
            }
            startChild.setArrowDegree(angle);
            startChild.invalidate();
        }
    }

    private void drawWhenTouchUp() {
        mPaint.setColor(isCorrect ? mColorCorrect : mColorIncorrect);
        mPaint.setAlpha(50);
        Log.d(TAG, "mChoose = " + mChoose);
        // 将终点设置位置为起点，即取消指引线
        mCurrentPoint.x = mLastPoint.x;
        mCurrentPoint.y = mLastPoint.y;
        // 改变子元素的状态为UP
        setItemModeUp();
        if (mChoose.size() > 0) {
            GestureLockNodeView lastGestureLockNodeView = (GestureLockNodeView) findViewById(mChoose.get(mChoose.size() - 1));
            lastGestureLockNodeView.hideArrow();
        }
    }

    private void settingPassword() {
        if (TextUtils.isEmpty(mFirstInputPassword)) {
            if (mSettingListener.onFirstInputComplete(mCurrentChooseString.length())) {
                mFirstInputPassword = mCurrentChooseString;
            }
        } else {
            boolean isGesturePasswordSetup = mFirstInputPassword.equals(mCurrentChooseString);
            mSettingListener.onSecondInputComplete(isGesturePasswordSetup);
            if (isGesturePasswordSetup) {
                sPasswordProvider.setPassword(mCurrentChooseString);
            }
        }
        reset();
    }

    private void setItemModeUp() {
        for (GestureLockNodeView gestureLockNodeView : mGestureLockNodeViews) {
            if (mChoose.contains(gestureLockNodeView.getId())) {
                gestureLockNodeView.setMode(GestureLockNodeView.Statu.STATU_TOUCH_UP);
            }
        }
    }


    /**
     * 通过x,y获得落入的GestureLockView
     *
     * @param x
     * @param y
     * @return
     */
    private GestureLockNodeView getChildIdByPos(int x, int y) {
        for (GestureLockNodeView gestureLockNodeView : mGestureLockNodeViews) {
            if (checkPositionInChild(gestureLockNodeView, x, y)) {
                return gestureLockNodeView;
            }
        }
        return null;
    }

    /**
     * 检查当前是否在child中
     *
     * @param child
     * @param x
     * @param y
     * @return
     */
    private boolean checkPositionInChild(View child, int x, int y) {

        //设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围变大，或者不设置padding
        int padding = (int) (child.getWidth() * 0.15);

        if (x >= child.getLeft() + padding && x <= child.getRight() - padding && y >= child.getTop() + padding && y <= child.getBottom() - padding) {
            return true;
        }
        return false;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
        //绘制指引线
        if (mChoose.size() > 0) {
            if (mLastPoint.x != 0 && mLastPoint.y != 0)
                canvas.drawLine(mLastPoint.x, mLastPoint.y, mCurrentPoint.x, mCurrentPoint.y, mPaint);
        }
    }

    /**
     * 做一些必要的重置
     */
    private void reset() {
        mChoose.clear();
        mCurrentChooseString = "";
        mPath.reset();
        for (GestureLockNodeView gestureLockNodeView : mGestureLockNodeViews) {
            gestureLockNodeView.setMode(GestureLockNodeView.Statu.STATU_DEFAULT);
            gestureLockNodeView.hideArrow();
        }
    }


    /**
     * 设置指引线宽度
     *
     * @param lineWidth 指引线宽度
     */
    //对外公开的一些方法
    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
    }

    /**
     * 设置密码验证回调
     *
     * @param verifyListener 密码验证回调接口
     */
    public void setVerifyListener(VerifyListener verifyListener) {
        this.mVerifyListener = verifyListener;
    }

    /**
     * 设置密码设置回调
     *
     * @param settingListener 密码设置回调接口
     */
    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    /**
     * 设置密码提供者
     *
     * @param passwordProvider 密码提供者
     */
    public static void setPasswordProvider(PasswordProvider passwordProvider) {
        sPasswordProvider = passwordProvider;
    }

    /**
     * 设置NodeView提供者
     *
     * @param nodeViewProvider nodeView提供者
     */
    public void setNodeViewProvider(NodeViewProvider nodeViewProvider) {
        mNodeViewProvider = nodeViewProvider;
    }

    /**
     * 获取密码提供者
     *
     * @return 密码提供者，默认为PasswordProviderDefault的实例
     */
    public static PasswordProvider getPasswordProvider() {
        if (sPasswordProvider == null) {
            sPasswordProvider = PasswordProviderDefault.getInstance(AppUtils.getApplicationContext());
        }
        return sPasswordProvider;
    }

    /**
     * 重置手势锁及密码
     */
    public void resetView() {
        reset();
        invalidate();
    }

}
