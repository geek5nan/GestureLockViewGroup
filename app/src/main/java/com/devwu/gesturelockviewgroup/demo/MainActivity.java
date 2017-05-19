package com.devwu.gesturelockviewgroup.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devwu.gesturelockviewgroup.GestureLockViewGroup;
import com.devwu.gesturelockviewgroupdemo.R;
import com.devwu.utils.ViewUtil;

public class MainActivity extends AppCompatActivity implements TextView.OnClickListener {

    TextView mLeftButton;
    TextView mRightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLeftButton = (TextView) findViewById(R.id.left_button);
        mRightButton = (TextView) findViewById(R.id.right_button);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GestureLockViewGroup.getPasswordProvider().hasPassword()) {
            mLeftButton.setText("Verify Lock");
            ViewUtil.show(mRightButton);
        } else {
            mLeftButton.setText("Setting Lock");
            ViewUtil.hide(mRightButton);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_button) {
            GestureLockActivity.actionStart(this, GestureLockViewGroup.getPasswordProvider().hasPassword() ? GestureLockActivity.STATE.VERIFY : GestureLockActivity.STATE.SETTING);
        } else {
            GestureLockActivity.actionStart(this, GestureLockActivity.STATE.RESET);
        }
    }
}
