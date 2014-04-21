package com.agcy.wikiread;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.agcy.wikiread.Adapters.LangSelectorAdapter;
import com.agcy.wikiread.Adapters.LangSwitcherAdapter;
import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Core.Api.SearchItem;
import com.agcy.wikiread.Core.Api.SearchSuggestion;
import com.agcy.wikiread.Core.Helper;
import com.agcy.wikiread.Core.Loader;
import com.agcy.wikiread.Core.Pager;
import com.agcy.wikiread.Core.Parsing.Url;
import com.agcy.wikiread.Fragments.LoaderFragment;
import com.agcy.wikiread.Fragments.PageFragment;
import com.agcy.wikiread.Fragments.SearchFragment;
import com.agcy.wikiread.Models.LangLink;
import com.agcy.wikiread.Models.Page;
import com.agcy.wikiread.Views.Drawer;
import com.agcy.wikiread.Views.DrawerMainMenu;
import com.agcy.wikiread.Views.LangSwitcherView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;

import java.util.ArrayList;

public class PageActivity extends Activity {

    Page page;
    Fragment currentFragment;
    Fragment tempFragment;
    Context context;
    Drawer drawer;
    View langSwitcherView;
    View drawerMenu;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_page);
        this.context = this;

        drawer = (Drawer) findViewById(R.id.drawer);

        EditText search = (EditText) findViewById(R.id.search);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null &&
                        (event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search(String.valueOf(v.getText()));
                }
                return false;
            }
        });


        Intent intent = getIntent();
        Uri uri = intent.getData();

        String url = uri.toString();
        this.lang = Url.parseLangFromUrl(url);
        setContent(Url.parseUrl(url));


    }
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);  // Add this method.

    }
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);  // Add this method.
    }
    public void setContent(String title, String lang) {
        this.lang = lang;
        setContent(Url.getApiUrl(title, lang));
    }
    public void setContent(final String url){

        this.context = this;
        tempFragment = new LoaderFragment("Loading") {
            @Override
            public void onFinish() {
                replaceFragment();
            }

            @Override
            public void onError(Exception exp) {
                if(Helper.isTest())
                    updateStatus(exp.getLocalizedMessage()+ "\n " + url, false);
                else
                    updateStatus("Error has occurred", false);
            }
        };
        replaceFragment();

        Loader task = new Loader(Loader.PAGE) {

            @Override
            public void onSuccess(Object response) {

                onPageLoaded((Api) response);
            }

            @Override
            public void onError(Exception exp) {
                //todo: fragment reload

                ((LoaderFragment) currentFragment).onError(exp);
            }

            @Override
            public void onFinish() {

            }

        };
        task.execute(url);
    }


    public void onPageLoaded(final Api response) {

        new AsyncTask<Object, Void, Exception>() {
            @Override
            protected Exception doInBackground(Object... params) {
                try {
                    page = Pager.getPage(response);
                    tempFragment = new PageFragment(page, context, lang);
                    LangSwitcherView langSwitcherViewTemp = ((PageFragment) tempFragment).parseLangs();
                    langSwitcherViewTemp.setOnClickListener(new LangSwitcherAdapter.OnClickListener() {
                        @Override
                        public void onClick(Object object) {
                            LangLink langLink = (LangLink) object;
                            languageChanged(langLink.lang, langLink.title);
                            drawer.close();
                        }
                    });
                    langSwitcherView = langSwitcherViewTemp;
                    ((PageFragment) tempFragment).parseViews();
                    ((PageFragment) tempFragment).fetchImagesUrls();

                } catch (Exception exp) {
                    return exp;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception exception) {
                if(exception==null)
                    ((LoaderFragment) currentFragment).onLoaded(true);
                else
                    ((LoaderFragment) currentFragment).onError(exception);

            }
        }.execute();

        ((LoaderFragment) currentFragment).updateStatus("Preparing readability", true);

    }

    public void replaceFragment() {
        currentFragment = tempFragment;
        FragmentManager manager = getFragmentManager();
        if (manager != null)
            manager.beginTransaction()
                    .replace(R.id.container, currentFragment)
                    .commit();
    }

    public void onGlobeClick(View view) {
            if(langSwitcherView!=null) {
                drawer.setContent(langSwitcherView);
                drawer.setSide(Drawer.SIDE_RIGHT);
                drawer.open();
            }

    }
    public void languageChanged(String lang, String title){
        this.lang = lang;
        setContent(title, lang);
        drawer.close();
    }

    public void onDrawerClick(View view) {
            drawerMenu = new DrawerMainMenu(context);
            drawer.setContent(drawerMenu);
            drawer.setSide(Drawer.SIDE_LEFT);
            drawer.open();
        Intent intent = new Intent(context, SettingsActivity.class);
        //startActivity(intent);
    }



    public void search(final String searchRequest) {

        String url = Url.getSearchUrl(searchRequest, lang);

        langSwitcherView = null;



        this.context = this;
        tempFragment = new LoaderFragment("Searching") {
            @Override
            public void onFinish() {
                replaceFragment();
            }

            @Override
            public void onError(Exception exp) {
                updateStatus(exp.getLocalizedMessage(), false);
            }
        };
        replaceFragment();

        Loader task = new Loader(Loader.SEARCH) {

            @Override
            public void onSuccess(Object response) {
                ArrayList<SearchItem> items = new ArrayList<SearchItem>(((SearchSuggestion) response).searchItemList);
                tempFragment = new SearchFragment(items, context);

                ListView langSwitcherViewTemp = new ListView(context);
                langSwitcherViewTemp.setAdapter(new LangSelectorAdapter(context));
                langSwitcherViewTemp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        lang = ((LangLink) parent.getItemAtPosition(position)).lang;
                        search(searchRequest);
                        drawer.close();
                    }
                });
                langSwitcherView = langSwitcherViewTemp;
                ((LoaderFragment) currentFragment).onLoaded(true);
            }

            @Override
            public void onError(Exception exp) {
                //todo: fragment reload

                ((LoaderFragment) currentFragment).onError(exp);
            }

            @Override
            public void onFinish() {

            }

        };
        task.execute(url);
    }


}