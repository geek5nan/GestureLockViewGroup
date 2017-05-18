## GestureLockViewGroup

### 介绍
**GestureLockViewGroup**是一个基于[GestureLock](https://github.com/autume/GestureLock.git)重构的手势锁控件，目标是打造为一个高内聚，低耦合，易于扩展的控件

>### 原项目特性
* 支持手势密码的绘制，并支持密码保存功能，解锁时自动比对密码给出结果
* 封装了绘制密码的方法，比对两次密码是否一致，可以快捷地进行手势密码的设置
* 可以设置密码输入错误后的重试次数上限
* 可以自定义不同状态下手势密码图案的颜色
* 可以自定义手势密码的触摸点数量（n*n）

### 功能改动
* 抽象手势密码接口`PasswordProvider`，便于扩展
* 抽象节点视图`GestureLockNodeView`，便于扩展
* 将密码错误次数暴露给外部调用者，由调用者自行处理
* 移除冗余变量
* 新增指引线宽度设置
* 新增NodeViewProviderDraw、NodeViewProviderImage用于构造节点视图

### 使用方式
1.添加GestureLockViewGroup

```Java
<com.devwu.gesturelockviewgroup.GestureLockViewGroup
        android:id="@+id/gesture_lock_view_group"
        android:layout_height="match_parent"
        android:layout_width="match_parent"/>
```
2.设置密码验证回调方法

````Java
mGestureLockViewGroup.setVerifyListener(new VerifyListener() {
    @Override
    public void onGestureVerify(boolean matched, int retryTimes) {

    }
});
````

3.设置密码设置回调方法

````Java
mGestureLockViewGroup.setSettingListener(new SettingListener() {
    @Override
    public boolean onFirstInputComplete(int len) {
 
    }

    @Override
    public void onSecondInputComplete(boolean matched) {

    }
});
````

4.自定义NodeView,详见Builder类

```Java
//加载图片作为NodeView
mGestureLockViewGroup.setNodeViewProvider(new NodeViewProviderImage.Builder(this).build());
//使用绘制的NodeView
mGestureLockViewGroup.setNodeViewProvider(new NodeViewProviderDraw.Builder(this).build());

```

5.自定义PasswordProvider,默认为PasswordProviderDefault

```Java
GestureLockViewGroup.setPasswordProvider(new PasswordProvider() {
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public boolean hasPassword() {
        return false;
    }

    @Override
    public void removePassword() {

    }
});
```





### 导入方法
##### 1.在项目根部的`build.gradle`中添加`JitPack`仓库

````Java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
````

##### 2. 在应用模块中添加如下依赖

````Java
dependencies {
        ...
        compile 'com.github.geek5nan:GestureLockViewGroup:1.0'
}
````
### 鸣谢

源码提供 [@autume](https://github.com/autume)

技术支持 [@shenbei1992](https://github.com/shenbei1992)
