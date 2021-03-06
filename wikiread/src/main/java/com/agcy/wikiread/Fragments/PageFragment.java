package com.agcy.wikiread.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agcy.wikiread.Core.ImageUrlFetcher;
import com.agcy.wikiread.Core.Pager;
import com.agcy.wikiread.Models.Page;
import com.agcy.wikiread.R;
import com.agcy.wikiread.Views.LangSwitcherView;
import com.agcy.wikiread.Views.PictureView;

import java.util.ArrayList;

public class PageFragment extends Fragment {
    Page page;
    View rootView;
    LangSwitcherView languagesView;
    Context context;
    Pager parser;
    public String lang;
    public ImageUrlFetcher imageUrlFetcher;

    public PageFragment() {

    }

    public PageFragment(Page page, Context context, String lang) {

        this.page = page;
        this.context = context;
        this.lang = lang;
        parser = new Pager(page, lang);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_page, container, false);
        LinearLayout contentView = (LinearLayout) rootView.findViewById(R.id.container);
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        titleView.setText(page.title);
        for (View view : parser.parsedViews)
            contentView.addView(view);

        return rootView;
    }

    public void parseViews() {
        parser.parseViews(context);
    }

    public void fetchImagesUrls() {

        final ArrayList<PictureView> seekers = diveAndFindSeekers(parser.parsedViews);

        imageUrlFetcher = new ImageUrlFetcher(seekers,lang) {

            @Override
            public void onFetched() {
                downloadImages(seekers);
            }
        };

        imageUrlFetcher.execute();


    }

    public void downloadImages(ArrayList<PictureView> listeners) {
        for (View listener : listeners) {
            ((PictureView) listener).startLoading();
        }
    }

    public ArrayList<PictureView> diveAndFindSeekers(ArrayList<View> views) {

        ArrayList<PictureView> seekers = new ArrayList<PictureView>();
        for (View view : views) {

            if (view instanceof PictureView) {
                seekers.add((PictureView) view);
            }
            if (view instanceof ViewGroup) {
                int count = ((ViewGroup) view).getChildCount();
                ArrayList<View> diveIntoMe = new ArrayList<View>();
                for (int i = 0; i < count; i++) {
                    View child = ((LinearLayout) view).getChildAt(i);
                    diveIntoMe.add(child);
                }
                seekers.addAll(diveAndFindSeekers(diveIntoMe));
            }

        }
        return seekers;
    }

    public LangSwitcherView parseLangs() {
        if (languagesView == null) {
            languagesView = new LangSwitcherView(context, page.langlinks, lang, true);
        }
        return languagesView;
    }
}
