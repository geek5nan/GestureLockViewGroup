package com.devwu.gesturelockviewgroup.provider.nodeview;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;

import com.devwu.gesturelockviewgroup.R;

import com.devwu.gesturelockviewgroup.nodeview.GestureLockNodeView;
import com.devwu.gesturelockviewgroup.nodeview.GestureLockNodeViewImage;


/**
 * Created by WuNan on 17/5/16.
 *
 */

public class NodeViewProviderImage implements NodeViewProvider {
    private Context mContext;

    private int mLockIconBgDrawableId;
    private int mLockIconArrowDrawableId;
    private int mLockIconBgDrawableDefault;
    private int mLockIconBgDrawableMoving;
    private int mLockIconBgDrawableIncorrect;
    private int mLockIconBgDrawableCorrect;
    private int mLockIconArrowDrawableDefault;
    private int mLockIconArrowDrawableMoving;
    private int mLockIconArrowDrawableIncorrect;
    private int mLockIconArrowDrawableCorrect;

    public static class Builder {
        Context mContext;

        private int mLockIconBgDrawableId = 0;
        private int mLockIconArrowDrawableId = 0;
        private int mLockIconBgDrawableDefault = R.drawable.lock_icon_bg_default;
        private int mLockIconBgDrawableMoving = R.drawable.lock_icon_bg_moving;
        private int mLockIconBgDrawableIncorrect = R.drawable.lock_icon_bg_incorrect;
        private int mLockIconBgDrawableCorrect = R.drawable.lock_icon_bg_correct;
        private int mLockIconArrowDrawableDefault = R.drawable.lock_icon_arrow_default;
        private int mLockIconArrowDrawableMoving = R.drawable.lock_icon_arrow_moving;
        private int mLockIconArrowDrawableIncorrect = R.drawable.lock_icon_arrow_incorrect;
        private int mLockIconArrowDrawableCorrect = R.drawable.lock_icon_arrow_correct;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setLockIconDrawableIds(int lockIconBgDrawableId,int lockIconArrowDrawableId) {
            mLockIconBgDrawableId = lockIconBgDrawableId;
            mLockIconArrowDrawableId = lockIconArrowDrawableId;
            return this;
        }

        public Builder setLockIconBgDrawableDefault(int lockIconBgDrawableDefault, int lockIconBgDrawableMoving, int lockIconBgDrawableIncorrect, int lockIconBgDrawableCorrect) {
            this.mLockIconBgDrawableDefault = lockIconBgDrawableDefault;
            this.mLockIconBgDrawableMoving = lockIconBgDrawableMoving;
            this.mLockIconBgDrawableIncorrect = lockIconBgDrawableIncorrect;
            this.mLockIconBgDrawableCorrect = lockIconBgDrawableCorrect;
            return this;
        }

        public Builder setLockIconArrowDrawableDefault(int lockIconArrowDrawableDefault, int lockIconArrowDrawableMoving, int lockIconArrowDrawableIncorrect, int lockIconArrowDrawableCorrect) {
            this.mLockIconArrowDrawableDefault = lockIconArrowDrawableDefault;
            this.mLockIconArrowDrawableMoving = lockIconArrowDrawableMoving;
            this.mLockIconArrowDrawableIncorrect = lockIconArrowDrawableIncorrect;
            this.mLockIconArrowDrawableCorrect = lockIconArrowDrawableCorrect;
            return this;
        }


        public NodeViewProviderImage build() {
            NodeViewProviderImage childViewProviderImage = new NodeViewProviderImage();
            childViewProviderImage.mContext = mContext;
            if (mLockIconBgDrawableId * mLockIconArrowDrawableId > 0) {
                childViewProviderImage.mLockIconBgDrawableId = mLockIconBgDrawableId;
                childViewProviderImage.mLockIconArrowDrawableId = mLockIconArrowDrawableId;
                return childViewProviderImage;
            }
            childViewProviderImage.mLockIconBgDrawableDefault = mLockIconBgDrawableDefault;
            childViewProviderImage.mLockIconBgDrawableMoving = mLockIconBgDrawableMoving;
            childViewProviderImage.mLockIconBgDrawableIncorrect = mLockIconBgDrawableIncorrect;
            childViewProviderImage.mLockIconBgDrawableCorrect = mLockIconBgDrawableCorrect;

            childViewProviderImage.mLockIconArrowDrawableDefault = mLockIconArrowDrawableDefault;
            childViewProviderImage.mLockIconArrowDrawableMoving = mLockIconArrowDrawableMoving;
            childViewProviderImage.mLockIconArrowDrawableIncorrect = mLockIconArrowDrawableIncorrect;
            childViewProviderImage.mLockIconArrowDrawableCorrect = mLockIconArrowDrawableCorrect;

            return childViewProviderImage;
        }
    }

    private StateListDrawable makeStateListDrawable(Context context, @DrawableRes int drawableDefault, @DrawableRes int drawableMoving, @DrawableRes int drawableIncorrect, @DrawableRes int drawableCorrect) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(GestureLockNodeViewImage.stateDefault, context.getResources().getDrawable(drawableDefault));
        drawable.addState(GestureLockNodeViewImage.stateMoving, context.getResources().getDrawable(drawableMoving));
        drawable.addState(GestureLockNodeViewImage.stateIncorrect, context.getResources().getDrawable(drawableIncorrect));
        drawable.addState(GestureLockNodeViewImage.stateCorrect, context.getResources().getDrawable(drawableCorrect));
        return drawable;
    }

    @Override
    public GestureLockNodeView initChildView() {
        if (mLockIconArrowDrawableId * mLockIconArrowDrawableId > 0) {
            return GestureLockNodeViewImage.create(mContext, mLockIconBgDrawableId, mLockIconArrowDrawableId);
        } else {
            StateListDrawable lockIconBgDrawable = makeStateListDrawable(mContext, mLockIconBgDrawableDefault, mLockIconBgDrawableMoving, mLockIconBgDrawableIncorrect, mLockIconBgDrawableCorrect);
            StateListDrawable lockIconArrowDrawable = makeStateListDrawable(mContext, mLockIconArrowDrawableDefault, mLockIconArrowDrawableMoving, mLockIconArrowDrawableIncorrect, mLockIconArrowDrawableCorrect);
            return GestureLockNodeViewImage.create(mContext, lockIconBgDrawable, lockIconArrowDrawable);
        }

    }
}
