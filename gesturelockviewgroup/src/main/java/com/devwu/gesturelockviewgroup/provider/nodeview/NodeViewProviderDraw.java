package com.devwu.gesturelockviewgroup.provider.nodeview;

import android.content.Context;

import com.devwu.gesturelockviewgroup.nodeview.GestureLockNodeView;
import com.devwu.gesturelockviewgroup.nodeview.GestureLockNodeViewDraw;


/**
 * Created by WuNan on 17/5/16.
 */

public class NodeViewProviderDraw implements NodeViewProvider {
    Context mContext;
    private int mColorDefault;
    private int mColorMoving ;
    private int mColorCorrect;
    private int mColorIncorrect;

    public static class Builder{
        Context mContext;
        int mColorDefault;
        int mColorMoving;
        int mColorCorrect;
        int mColorIncorrect;
        public Builder(Context context) {
            mContext = context;
        }
        public Builder setColorDefault(int colorDefault){
            mColorDefault = colorDefault;
            return this;
        }
        public Builder setColorMoving(int colorMoving){
            mColorMoving = colorMoving;
            return this;
        }
        public Builder setColorIncorrect(int colorIncorrect){
            mColorIncorrect = colorIncorrect;
            return this;
        }
        public Builder setColorCorrect(int colorCorrect){
            mColorCorrect = colorCorrect;
            return this;
        }

        public NodeViewProvider build(){
            NodeViewProviderDraw childViewProviderDraw = new NodeViewProviderDraw();
            childViewProviderDraw.mContext = mContext;
            childViewProviderDraw.mColorDefault = mColorDefault;
            childViewProviderDraw.mColorMoving = mColorMoving;
            childViewProviderDraw.mColorIncorrect = mColorIncorrect;
            childViewProviderDraw.mColorCorrect = mColorCorrect;
            return childViewProviderDraw;
        }
    }

    @Override
    public GestureLockNodeView initChildView() {
        return new GestureLockNodeViewDraw(mContext,mColorDefault, mColorMoving,mColorCorrect, mColorIncorrect);
    }
}
