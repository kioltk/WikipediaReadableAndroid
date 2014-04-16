package com.agcy.wikiread.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agcy.wikiread.R;

/**
 * Created by kiolt_000 on 06.03.14.
 */
public abstract class LoaderFragment extends Fragment {
    View rootView;
    String status;
    public LoaderFragment(String status){
        this.status = status;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_loading, container, false);
        LinearLayout contentView = (LinearLayout) rootView.findViewById(R.id.container);
        updateStatus(status,true);

        return rootView;
    }
    public void updateStatus(String status,Boolean isBlinking){
        TextView titleView = (TextView) rootView.findViewById(R.id.status);
        titleView.setText(status);
        if(isBlinking){
            final Animation blinking = new AlphaAnimation(1, (float) 0.6);
            blinking.setInterpolator(new CycleInterpolator((float) 0.5));
            blinking.setDuration(2000);
            blinking.setRepeatCount(Animation.INFINITE);
            blinking.setRepeatMode(Animation.INFINITE);



            rootView.startAnimation(blinking);
        }
        else{
            rootView.clearAnimation();
        }

    }
    public void onLoaded(Boolean success){
        if(success){
            final Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateDecelerateInterpolator()); // and this
            fadeOut.setStartOffset(0);
            fadeOut.setDuration(1000);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onFinish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rootView.clearAnimation();
            rootView.startAnimation(fadeOut);
        }else{
            updateStatus("Error has occurred",false);
        }
    }
    public abstract void onFinish();
    public abstract void onError(Exception exp);

}
