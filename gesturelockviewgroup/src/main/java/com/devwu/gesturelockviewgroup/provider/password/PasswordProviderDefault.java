package com.devwu.gesturelockviewgroup.provider.password;

import android.content.Context;
import android.text.TextUtils;

import com.devwu.common.utils.SharePreferenceUtil;


/**
 * Created by WuNan on 17/5/11.
 * 内置密码提供者
 */

public class PasswordProviderDefault extends SharePreferenceUtil implements PasswordProvider {
    private static String DATA_NAME = "gesture_password_provider";
    private static PasswordProviderDefault sPasswordProviderDefault = null;

    public static PasswordProviderDefault getInstance(Context context) {
        if (sPasswordProviderDefault == null) {
            sPasswordProviderDefault = new PasswordProviderDefault(context.getApplicationContext(), DATA_NAME);
        }
        return sPasswordProviderDefault;
    }

    private PasswordProviderDefault(Context context, String dataName) {
        super(context, dataName);
    }

    @Override
    public String getPassword() {
        return getString(DATA_NAME);
    }

    @Override
    public void setPassword(String password) {
        if (TextUtils.isEmpty(password) && hasKey(DATA_NAME)){
            remove(DATA_NAME);
        }
        else {
            putString(DATA_NAME, password);
        }
    }

    @Override
    public boolean hasPassword() {
        return hasKey(DATA_NAME);
    }

    @Override
    public void removePassword() {
        remove(DATA_NAME);
    }
}
