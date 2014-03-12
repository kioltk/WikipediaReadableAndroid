package com.agcy.wikiread.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agcy.wikiread.Models.Image;
import com.agcy.wikiread.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by kiolt_000 on 03.03.14.
 */
public class PictureView extends LinearLayout {
    public ImageView imageView;
    public TextView textView;
    private Image image;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public PictureView(Context context) {
        super(context);
        image = new Image();

        Resources r = getResources();
        setOrientation(VERTICAL);
        setBackground(r.getDrawable(R.drawable.border));
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, px, 0, px);
        setPadding(35, 30, 35, 30);
        setLayoutParams(params);

        imageView = new ImageView(context);
        textView = new TextView(context);

        textView.setPadding(0,15,0,0);

        imageView.setAdjustViewBounds(true);
        imageView.setImageDrawable(r.getDrawable(R.drawable.logo));
        standartSettings();

        addView(imageView);
        addView(textView);
    }
    public void setImageName(String name){
        image.title = name;
    }
    public String getImageName() {
        return image.title;
    }
    public void setDescription(Spannable description){
        textView.setText(description);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public Image getImage() {
        return image;
    }
    //public void setImage(Image image){
    //    this.image = image;
    //}

    public void standartSettings(){
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setPadding(150,150,150,150);
    }

    public void loaded(){
        Log.i("wiki","downloading finished " + image.url);
        imageView.clearAnimation();
        imageView.setOnClickListener(null);
        imageView.setPadding(0,0,0,0);
    }
    public void loadingStarted(){
        standartSettings();
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        final Animation blinking = new AlphaAnimation(1, (float) 0.6);
        blinking.setInterpolator(new CycleInterpolator((float) 0.5));
        blinking.setDuration(2000);
        blinking.setRepeatCount(Animation.INFINITE);
        blinking.setRepeatMode(Animation.INFINITE);

        imageView.startAnimation(blinking);
        imageView.setOnClickListener(null);
        Log.i("wiki","downloading started " + image.url);

    }
    public void errored(){
        standartSettings();
        imageView.clearAnimation();
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.error));
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"imhere",Toast.LENGTH_SHORT).show();
                startLoading();
            }
        });
        imageView.setClickable(true);
    }

    public void startLoading() {
        ImageLoader.getInstance().displayImage(image.url,imageView,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                loadingStarted();

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                errored();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmap==null)
                    errored();//todo: why always null?
                loaded();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
}
