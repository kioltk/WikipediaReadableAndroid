package com.agcy.wikiread.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.agcy.wikiread.R;

/**
 * Created by kiolt_000 on 15-Apr-14.
 */
public class Drawer extends LinearLayout {
    public static final int SIDE_RIGHT = 1;
    public static final int SIDE_LEFT = 0;
    protected int side = 0;
    protected View paddingView;
    protected View content;
    protected boolean isOpened = false;
    public Drawer(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setVisibility(GONE);
        setBackgroundColor(0xAA000000);
        setOrientation(HORIZONTAL);
        if(!isInEditMode()) {
            addNonEditModeSettings();
        }
        paddingView = new View(getContext());
        paddingView.setLayoutParams(new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,3f));
        //todo: define width to tablets

        setContent(new View(context));
        content.setBackgroundColor(0xFFFFFFFF);



    }
    public void addNonEditModeSettings(){

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    public void setContent(View content) {

        this.content = content;
        content.setBackgroundColor(0xFFFFFFFF);
        content.setLayoutParams(new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 7f ));

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

        removeAllViewsInLayout();

        if(isLeft()) {
            addView(content);
            addView(paddingView);
        }else {
            addView(paddingView);
            addView(content);
        }

        isOpened = true;
        setVisibility(VISIBLE);

        startAnimation(new AlphaAnimation(0,1));
        getAnimation().setDuration(400);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(new TranslateAnimation((isLeft() ? -300 : 300),0,0,0));
        set.addAnimation(new AlphaAnimation(0,1));
        set.setStartOffset(100);
        set.setInterpolator(new DecelerateInterpolator(0.8f));
        set.setDuration(400);

        content.startAnimation(set);
    }
    public void close() {
        isOpened= false;

        startAnimation(new AlphaAnimation(1,0));
        getAnimation().setDuration(400);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(new TranslateAnimation(0, (isLeft() ? -300 : 300), 0, 0));
        set.addAnimation(new AlphaAnimation(1, 0));
        set.setInterpolator(new DecelerateInterpolator(0.8f));
        set.setDuration(400);

        content.startAnimation(set);

        getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
