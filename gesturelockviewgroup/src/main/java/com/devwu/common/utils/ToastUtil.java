package com.devwu.common.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;


public class ToastUtil {

    public static void showShort(@NonNull CharSequence sequence) {
        showToast(sequence.toString(), Toast.LENGTH_SHORT);
    }

    public static void showShort(int strID) {
        showToast(strID, Toast.LENGTH_SHORT);
    }

    public static void showLong(@NonNull CharSequence sequence) {
        showToast(sequence.toString(), Toast.LENGTH_LONG);
    }

    public static void showLong(int strID) {
        showToast(strID, Toast.LENGTH_LONG);
    }


    private static void showToast(String toastStr, int length) {
        if (checkText(toastStr)) {
            Toast.makeText(AppUtils.getApplicationContext(), toastStr, length).show();
        }
    }

    private static void showToast(int id, int length) {
        if (checkText(AppUtils.getApplicationContext().getString(id))) {
            Toast.makeText(AppUtils.getApplicationContext(), id, length).show();
        }
    }

    private static boolean checkText(String string) {
        return !TextUtils.isEmpty(string);
    }

}
