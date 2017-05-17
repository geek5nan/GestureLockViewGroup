package com.devwu.gesturelockviewgroup.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.devwu.common.utils.ToastUtil;
import com.devwu.gesturelockviewgroup.GestureLockViewGroup;
import com.devwu.gesturelockviewgroup.listener.SettingListener;
import com.devwu.gesturelockviewgroup.listener.VerifyListener;
import com.devwu.gesturelockviewgroupdemo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by WuNan on 17/5/17.
 * 手势密码Activity示例
 */

public class GestureLockActivity extends AppCompatActivity {
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATE {
        int VERIFY = 0; //验证
        int RESET = 1;  //重置
        int SETTING = 2; //设置
    }

    public static void actionStart(Context context, @STATE int state) {
        Intent intent = new Intent(context, GestureLockActivity.class);
        intent.putExtra(CODE_STATE, state);
        context.startActivity(intent);
    }

    public final static String CODE_STATE = "code_state";
    int mState;
    GestureLockViewGroup mGestureLockViewGroup;
    TextView mPromptText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mState = getIntent().getIntExtra(CODE_STATE, STATE.VERIFY);
        initView();
        mGestureLockViewGroup.setVerifyListener(new VerifyListener() {
            @Override
            public void onGestureVerify(boolean matched, int retryTimes) {
                if (matched) {
                    if (mState == STATE.RESET) {
                        mGestureLockViewGroup.getPasswordProvider().removePassword();
                        mGestureLockViewGroup.resetView();
                        mPromptText.setText("请绘制新手势密码！");
                    }else {
                        mPromptText.setText("手势密码正确");
                        ToastUtil.showShort("手势密码正确");
                        finish();
                        if (mState == STATE.VERIFY){

                        }
                    }
                } else {
                    if (retryTimes > 3) {
                        mPromptText.setTextColor(Color.RED);
                        mPromptText.setText("错误次数过多，请稍后再试!");
                        ToastUtil.showShort("错误次数过多，请稍后再试!");
                        finish();
                    } else {
                        mPromptText.setTextColor(Color.RED);
                        mPromptText.setText("手势密码错误");
                    }
                }
            }
        });
        mGestureLockViewGroup.setSettingListener(new SettingListener() {
            @Override
            public boolean onFirstInputComplete(int len) {
                if (len > 3) {
                    mPromptText.setText("请再次绘制手势密码");
                    return true;
                } else {
                    mPromptText.setTextColor(Color.RED);
                    mPromptText.setText("最少连接4个点，请重新输入!");
                    return false;
                }
            }

            @Override
            public void onSecondInputComplete(boolean matched) {
                if (matched) {
                    finish();
                    if (mState == STATE.RESET) {
                        ToastUtil.showShort("手势密码修改成功!");
                    }
                    if (mState == STATE.SETTING) {
                        ToastUtil.showShort("手势密码设置成功!");
                    }
                } else {
                    ToastUtil.showShort("手势密码与第一次不同");
                }
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_gesture_lock);
        mPromptText = (TextView) findViewById(R.id.prompt_text);
        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.gesture_lock_view_group);
        switch (mState) {
            case STATE.VERIFY:
            {
                mPromptText.setText("请绘制手势密码进行验证");
            }
            break;
            case STATE.RESET: {
                mPromptText.setText("请输入原手势密码！");
            }
            break;
            case STATE.SETTING: {
                mPromptText.setText("请绘制手势密码");
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
