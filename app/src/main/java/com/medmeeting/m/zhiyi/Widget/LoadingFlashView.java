package com.medmeeting.m.zhiyi.Widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 13/01/2018 3:22 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class LoadingFlashView  extends FrameLayout {

    private View mFlashView;
    private ImageView mLoad1;
    private ImageView mLoad2;
    private ImageView mLoad3;
    private AnimatorSet mAnimatorSet;

    public LoadingFlashView(Context context) {
        this(context, null);
    }

    public LoadingFlashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingFlashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mFlashView = LayoutInflater.from(context).inflate(R.layout.loading_flash_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoad1 = ((ImageView) findViewById(R.id.load1));
        mLoad2 = ((ImageView) findViewById(R.id.load2));
        mLoad3 = ((ImageView) findViewById(R.id.load3));
    }

    public void showLoading() {
        if (getVisibility() != View.VISIBLE)
            return;
        if (mAnimatorSet == null)
            initAnimation();
        if (mAnimatorSet.isRunning() || mAnimatorSet.isStarted())
            return;
        mAnimatorSet.start();
    }

    public void hideLoading() {
        if (mAnimatorSet == null)
            return;
        if ((!mAnimatorSet.isRunning()) && (!mAnimatorSet.isStarted()))
            return;
        mAnimatorSet.removeAllListeners();
        mAnimatorSet.cancel();
        mAnimatorSet.end();
    }


    private void initAnimation() {
        PorterDuffColorFilter localPorterDuffColorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.dodgerblue), PorterDuff.Mode.SRC_ATOP); //subscribe_category_bar_bg_night
        mLoad1.setColorFilter(localPorterDuffColorFilter);
        mLoad2.setColorFilter(localPorterDuffColorFilter);
        mLoad3.setColorFilter(localPorterDuffColorFilter);


        mAnimatorSet = new AnimatorSet();

        List<ImageView> imageViewList = Arrays.asList(mLoad1, mLoad2, mLoad3);
        List<Animator> animatorList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ObjectAnimator loadAnimator = ObjectAnimator.ofFloat(imageViewList.get(i), "alpha", new float[]{1.0F, 0.5F}).setDuration(500L);
            loadAnimator.setStartDelay(100 * i);
            loadAnimator.setRepeatMode(ObjectAnimator.REVERSE);
            loadAnimator.setRepeatCount(-1);
            animatorList.add(loadAnimator);
        }
        mAnimatorSet.playTogether(animatorList);


//        ObjectAnimator loadAnimator1 = ObjectAnimator.ofFloat(mLoad1, "alpha", new float[]{1.0F, 0.5F}).setDuration(500L);
//        ObjectAnimator loadAnimator2 = ObjectAnimator.ofFloat(mLoad2, "alpha", new float[]{1.0F, 0.5F}).setDuration(500L);
//        ObjectAnimator loadAnimator3 = ObjectAnimator.ofFloat(mLoad3, "alpha", new float[]{1.0F, 0.5F}).setDuration(500L);
//        loadAnimator1.setStartDelay(0L);
//        loadAnimator2.setStartDelay(100L);
//        loadAnimator3.setStartDelay(200L);
//        loadAnimator1.setRepeatMode(ObjectAnimator.REVERSE);
//        loadAnimator2.setRepeatMode(ObjectAnimator.REVERSE);
//        loadAnimator3.setRepeatMode(ObjectAnimator.REVERSE);
//        loadAnimator1.setRepeatCount(-1);
//        loadAnimator2.setRepeatCount(-1);
//        loadAnimator3.setRepeatCount(-1);
//        mAnimatorSet.playTogether(new Animator[]{loadAnimator1, loadAnimator2, loadAnimator3, loadAnimator4});
    }

    public void setLoadingTheme(boolean isNight) {
        if (isNight) {
            PorterDuffColorFilter localPorterDuffColorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.subscribe_category_bar_bg_night), PorterDuff.Mode.SRC_ATOP);
            mLoad1.setColorFilter(localPorterDuffColorFilter);
            mLoad2.setColorFilter(localPorterDuffColorFilter);
            mLoad3.setColorFilter(localPorterDuffColorFilter);
        } else {
            mLoad1.setColorFilter(null);
            mLoad2.setColorFilter(null);
            mLoad3.setColorFilter(null);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE)
            hideLoading();
    }
}

