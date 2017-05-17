package com.devwu.gesturelockviewgroup.listener;

/**
 * 密码设置回调接口
 */
public interface SettingListener {
    /**
     * 首次输入密码
     * @param len    密码长度
     * @return 是否允许设置
     */
    boolean onFirstInputComplete(int len);

    /**
     * 第二次输入手势密码
     * @param matched 是否与第一次输入的密码匹配
     */
    void onSecondInputComplete(boolean matched);
}
