package com.devwu.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WuNan-QF on 16/5/27.
 */
public class SharePreferenceUtil {
    private SharedPreferences mPreferences;

    public SharePreferenceUtil(Context context, String dataName) {
       mPreferences = context.getSharedPreferences(dataName, Context.MODE_PRIVATE);
    }

    public void putString(String key, String val) {
        mPreferences.edit().putString(key, val).commit();
    }

    public String getString(String keyName) {
        return mPreferences.getString(keyName,"");
    }

    public void putInt(String key, int val) {
        mPreferences.edit().putInt(key, val).commit();
    }

    public int getInt(String keyName) {
        return mPreferences.getInt(keyName, 0);
    }

    public void putBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }
    public boolean getBoolean(String key, boolean defaultValue){
        return mPreferences.getBoolean(key,defaultValue);
    }

    public boolean hasKey(String key){
        return mPreferences.contains(key);
    }
    /**
     * @param key 需要删除对象的键名称
     */
    public void remove(String key) {
        remove(key, true);
    }

    /**
     * @param key   需要删除对象的键名称
     * @param apply 是否立即提交
     *              为true时，移出对象并立即提交事务
     *              为false时，需手动提交事务
     */
    public void remove(String key, boolean apply) {
        SharedPreferences.Editor editor = mPreferences.edit().remove(key);
        if (apply) {
            editor.commit();
        }
    }

    /**
     * 清空所有对象并立即提交事务
     */
    public void clear() {
        mPreferences.edit().clear().commit();
    }
}
