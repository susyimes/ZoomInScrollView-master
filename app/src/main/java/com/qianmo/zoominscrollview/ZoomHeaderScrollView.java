package com.qianmo.zoominscrollview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class ZoomHeaderScrollView extends NestedScrollView {
    private View mHeaderView;
    private int mHeaderWidth;
    private int mHeaderHeight;
    private OnPullZoomListener mOnPullZoomListener;
    private boolean mIsPulling;
    private int mLastY;

    private float mScaleRatio = 0.4f;

    private float mScaleTimes = 2.0f;

    private float mReplyRatio = 0.5f;

    boolean zoomEnable = true;

    public ZoomHeaderScrollView(Context context) {
        this(context, null);
    }

    public ZoomHeaderScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomHeaderScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setOverScrollMode(OVER_SCROLL_NEVER);
        View child = getChildAt(0);
        if (child != null && child instanceof ViewGroup) {
            mHeaderView = ((ViewGroup) child).getChildAt(0);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();
    }

    public void setOnPullZoomListener(OnPullZoomListener mOnPullZoomListener) {
        this.mOnPullZoomListener = mOnPullZoomListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHeaderView == null)
            return super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (zoomEnable) {
                    if (!mIsPulling) {
                        //第一次下拉
                        if (getScrollY() == 0) {
                            //在顶部的时候，记录顶部位置
                            mLastY = (int) ev.getY();
                        } else {
                            break;
                        }
                    }
                    if (ev.getY() - mLastY < 0)
                        return super.onTouchEvent(ev);
                    int distance = (int) ((ev.getY() - mLastY) * mScaleRatio);
                    mIsPulling = true;

                    setZoom(distance);

                    return true;
                }
            case MotionEvent.ACTION_UP:
                if (zoomEnable) {
                    mIsPulling = false;

                    replyView();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    private void setZoom(float s) {
        mOnPullZoomListener.onPullZooming(s);
        float scaleTimes = (float) ((mHeaderWidth + s) / (mHeaderWidth * 1.0));

        if (scaleTimes > mScaleTimes) return;

        ViewGroup.LayoutParams layoutParams = mHeaderView.getLayoutParams();
        layoutParams.width = (int) (mHeaderWidth + s);
        layoutParams.height = (int) (mHeaderHeight * ((mHeaderWidth + s) / mHeaderWidth));

        ((MarginLayoutParams) layoutParams).setMargins(-(layoutParams.width - mHeaderWidth) / 2, 0, 0, 0);
        mHeaderView.setLayoutParams(layoutParams);
    }


    private void replyView() {

        final float distance = mHeaderView.getMeasuredWidth() - mHeaderWidth;
        mOnPullZoomListener.onPullZoomEnd(distance);
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(distance, 0.0F).setDuration((long) (distance * mReplyRatio));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoom((Float) animation.getAnimatedValue());
            }
        });
        anim.start();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) onScrollListener.onScroll(l, t, oldl, oldt);
    }

    private OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setZoomEnable(boolean zoomEnable) {
        this.zoomEnable = zoomEnable;
    }

    public interface OnScrollListener {
        void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    public interface OnPullZoomListener {
        void onPullZooming(float newScrollValue);

        void onPullZoomEnd(float replyDistance);
    }

}
