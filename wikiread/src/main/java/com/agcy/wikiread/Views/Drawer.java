package com.agcy.wikiread.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.agcy.wikiread.R;

/**
 * Created by kiolt_000 on 15-Apr-14.
 */
public class Drawer extends LinearLayout {
    public static final int SIDE_RIGHT = 1;
    public static final int SIDE_LEFT = 0;
    protected int side = 0;
    protected View paddingView;
    protected LinearLayout contentContainer;
    protected View content;
    protected boolean isOpened = false;
    protected boolean isProcessing = false;
    public Drawer(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        contentContainer = new LinearLayout(context);
        contentContainer.setOrientation(VERTICAL);
        contentContainer.setBackgroundColor(0xFFFFFFFF);
        contentContainer.setLayoutParams(new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 7f ));

        paddingView = new View(getContext());
        paddingView.setLayoutParams(new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,3f));

        //todo: define width to tablets

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setVisibility(GONE);
        setBackgroundColor(0xAA000000);
        setOrientation(HORIZONTAL);
        if(!isInEditMode()) {
            addNonEditModeSettings();
        }

        setContent(new View(context));


    }
    private void addNonEditModeSettings(){

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    public void setContent(View content) {

        this.content = content;
        contentContainer.removeAllViews();
        contentContainer.addView(content);
    }
    public void setSide(int side){

        if (side==SIDE_LEFT || side == SIDE_RIGHT) {
            this.side= side;
        } else this.side = SIDE_LEFT;
    }

    public boolean isLeft(){
        return side == SIDE_LEFT;
    }
    public boolean isOpened(){
        return isOpened;
    }

    public void open() {

        if(isOpened)
            return;
        isProcessing = true;

        removeAllViewsInLayout();

        if(isLeft()) {
            addView(contentContainer);
            addView(paddingView);
        }else {
            addView(paddingView);
            addView(contentContainer);
        }

        setVisibility(VISIBLE);

        startAnimation(new AlphaAnimation(0,1));
        getAnimation().setDuration(200);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(new TranslateAnimation((isLeft() ? -300 : 300),0,0,0));
        set.addAnimation(new AlphaAnimation(0,1));
        set.setStartOffset(100);
        set.setInterpolator(new AccelerateInterpolator(0.8f));
        set.setDuration(200);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isProcessing = false;
                isOpened = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        contentContainer.startAnimation(set);
    }
    public void close() {
        if(!isOpened)
            return;
        isProcessing = true;
        startAnimation(new AlphaAnimation(1,0));
        getAnimation().setDuration(200);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(new TranslateAnimation(0, (isLeft() ? -300 : 300), 0, 0));
        set.addAnimation(new AlphaAnimation(1, 0));
        set.setInterpolator(new DecelerateInterpolator(0.8f));
        set.setDuration(200);

        contentContainer.startAnimation(set);

        getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);

                isProcessing = false;
                isOpened= false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
