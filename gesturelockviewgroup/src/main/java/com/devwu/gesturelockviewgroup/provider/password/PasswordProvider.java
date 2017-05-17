package com.devwu.gesturelockviewgroup.provider.password;

/**
 * Created by WuNan on 17/5/11.
 * 密码提供者接口
 */

public interface PasswordProvider {
    String getPassword();
    void setPassword(String password);
    boolean hasPassword();
    void removePassword();
}
