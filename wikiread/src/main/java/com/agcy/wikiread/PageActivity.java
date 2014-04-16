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
import android.widget.TextView;
import android.widget.Toast;

import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Core.Api.SearchItem;
import com.agcy.wikiread.Core.Api.SearchSuggestion;
import com.agcy.wikiread.Core.Helper;
import com.agcy.wikiread.Core.Loader;
import com.agcy.wikiread.Core.Parser;
import com.agcy.wikiread.Fragments.LoaderFragment;
import com.agcy.wikiread.Fragments.PageFragment;
import com.agcy.wikiread.Fragments.SearchFragment;
import com.agcy.wikiread.Models.LangLink;
import com.agcy.wikiread.Models.Page;
import com.agcy.wikiread.Views.Drawer;
import com.agcy.wikiread.Views.DrawerMainMenu;
import com.agcy.wikiread.Views.LangSwitcherView;

import java.util.ArrayList;

public class PageActivity extends Activity {

    Page page;
    Fragment currentFragment;
    Fragment tempFragment;
    Context context;
    Drawer drawer;
    LangSwitcherView langSwitcherView;
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
        lang = Parser.parseLangFromUrl(url);
        if(uri!=null)

            setContent(Parser.parseUrl(url));
        else
            setContent(Parser.parseTitleFromUrl(url),Parser.parseLangFromUrl(url));


    }

    public void setContent(String title, String lang) {
        setContent(Parser.getApiUrl(title,lang));
    }
    public void setContent(String url){

        this.context = this;
        tempFragment = new LoaderFragment("Loading") {
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

        new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                try {
                    page = Parser.getPage(response);
                    tempFragment = new PageFragment(page, context, lang);
                    langSwitcherView = ((PageFragment)tempFragment).parseLangs();
                    langSwitcherView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            LangLink langLink = (LangLink) parent.getItemAtPosition(position);
                            languageChanged(langLink.lang, langLink.title);
                        }
                    });
                    ((PageFragment) tempFragment).parseViews();
                    //todo: async parse
                    ((PageFragment) tempFragment).fetchImagesUrls();

                } catch (Exception exp) {
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                try {
                    ((LoaderFragment) currentFragment).onLoaded(success);
                } catch (Exception exp) {
                    ((LoaderFragment) currentFragment).onError(exp);
                }
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
        /*
            todo: change languages
            LangLink langLink = page.langlinks.get(1);
            String url = Parser.parseUrl(langLink.title, langLink.lang);
            setContent(url);
        */
    }
    public void languageChanged(String lang, String title){
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

        String url = Parser.getSearchUrl(searchRequest, lang);

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

                langSwitcherView = new LangSwitcherView(context);
                langSwitcherView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        lang = ((LangLink) parent.getItemAtPosition(position)).lang;
                        search(searchRequest);
                    }
                });
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