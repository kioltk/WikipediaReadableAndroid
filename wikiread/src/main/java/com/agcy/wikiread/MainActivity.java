package com.agcy.wikiread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Core.Helper;
import com.agcy.wikiread.Core.Loader;
import com.agcy.wikiread.Core.Parsing.Url;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Locale;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .build();
        ImageLoader.getInstance().init(config);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;



        final ImageView logoView = (ImageView) findViewById(R.id.logo);
        final View tipView = findViewById(R.id.tip);


        final Animation fadeInLogo = new AlphaAnimation(0, 1);
        fadeInLogo.setInterpolator(new AccelerateDecelerateInterpolator()); // and this
        fadeInLogo.setStartOffset(500);
        fadeInLogo.setDuration(1250);

        final Animation fadeInTip = new AlphaAnimation(0, 1);
        fadeInTip.setInterpolator(new AccelerateInterpolator()); // and this
        fadeInTip.setStartOffset(500);
        fadeInTip.setDuration(250);

        logoView.setAnimation(fadeInLogo);
        tipView.setAnimation(fadeInTip);

        fadeInLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                logoView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Intent intent = new Intent(context, PageActivity.class);

        final String lang = Locale.getDefault().getLanguage();
        Loader task = new Loader(Loader.PAGE) {

            @Override
            public void onSuccess(Object response) {


                String title =  ((Api) response).query.random.get(0).title;
                String url = "";
                if(!Helper.isTest())
                    url = Url.getApiUrl(title, lang);
                else
                    url = "http://ru.wikipedia.org/w/api.php?action=query&format=xml&prop=revisions|images|langlinks&rvprop=content&imlimit=500&lllimit=500&titles=Альберт Эйнштейн";
                intent.setData(Uri.parse(url));
            }

            @Override
            public void onError(Exception exp) {
            }

            @Override
            public void onFinish() {
                tipView.animate();
            }

        };


        task.execute(Url.getRandomUrl(lang));
        fadeInTip.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                tipView.setVisibility(View.VISIBLE);
                findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // первый поиск http://ru.wikipedia.org/wiki/Special:Search?search=ГОРИЛЛЫ&go=Go
        // http://www.wikipedia.org/search-redirect.php?family=wikipedia&search=ГОРИЛЛЫ&language=ru&go=++→++&go=Go
    }

}
