package com.qianmo.zoominscrollview;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

/**
 * Created by Susyimes on 2018/12/21.
 */

public class RefreshImageView extends AppCompatImageView {


    Animation operatingAnim;
    public RefreshImageView(Context context) {
        super(context);

    }

    public RefreshImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onDrag(float scrollvalue){
        this.setVisibility(View.VISIBLE);
        this.setRotation(scrollvalue);
    }

    public void onRefresh(){
        if (operatingAnim==null){
        operatingAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.loading_drawable);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);}
        this.setAnimation(operatingAnim);
        this.startAnimation(operatingAnim);
    }

    public void onRefreshEnd() {
        this.clearAnimation();
        this.setVisibility(INVISIBLE);
    }
}
