package com.devwu.gesturelockviewgroup.listener;

/**
 * 密码验证回调接口
 */
public interface VerifyListener {
    /**
     * 手势验证情况
     * @param matched 是否成功
     * @param retryTimes 重试次数,输入正确时清零
     */
    void onGestureVerify(boolean matched, int retryTimes);
}
