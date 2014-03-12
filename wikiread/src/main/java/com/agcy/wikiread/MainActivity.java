package com.agcy.wikiread;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
        fadeInTip.setStartOffset(1250);
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
                tipView.animate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeInTip.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                tipView.setVisibility(View.VISIBLE);
                findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PageActivity.class);

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
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

}
