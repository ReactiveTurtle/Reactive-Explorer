package ru.reactiveturtle.reactiveexplorer.fragments.tabs;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

public class ModifiedLinearSmoothScroller extends LinearSmoothScroller {
    private OnSmoothScrollListener mOnSmoothScrollListener;

    public ModifiedLinearSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mOnSmoothScrollListener != null)
            mOnSmoothScrollListener.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mOnSmoothScrollListener != null)
            mOnSmoothScrollListener.onStop();
    }

    public void setOnSmoothScrollListener(OnSmoothScrollListener onSmoothScrollListener) {
        mOnSmoothScrollListener = onSmoothScrollListener;
    }

    public interface OnSmoothScrollListener {
        void onStart();

        void onStop();
    }
}
