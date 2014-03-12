package com.agcy.wikiread;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Core.Loader;
import com.agcy.wikiread.Core.Parser;
import com.agcy.wikiread.Fragments.LoaderFragment;
import com.agcy.wikiread.Fragments.PageFragment;
import com.agcy.wikiread.Models.Page;

public class PageActivity extends Activity {

    Page page;
    Fragment currentFragment;
    Fragment tempFragment;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_page);


        Intent intent = getIntent();
        Uri uri = intent.getData();
        String url;
        if(uri!=null){
            url =  Parser.getApiUrl(uri.toString());
        }else
            url = Parser.getApiUrl("http://en.wikipedia.org/wiki/Africa");//Эйнштейн,%20Альберт");


        setContent(url);


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
                updateStatus(exp.getLocalizedMessage(),false);
            }
        };
        replaceFragment();

        Loader task = new Loader() {

            @Override
            public void onSuccess(Api response) {
                try{
                    onPageLoaded(response);
                    ((LoaderFragment)currentFragment).onLoaded(true);
                }
                catch (Exception exp){
                    ((LoaderFragment)currentFragment).onError(exp);
                }
            }

            @Override
            public void onError(Exception exp) {
                //todo: fragment reload

                ((LoaderFragment)currentFragment).onError(exp);
            }

            @Override
            public void onFinish() {

            }

        };
        task.execute(url);
    }
    public void onPageLoaded(Api response){

        page = Parser.getPage(response);
        tempFragment = new PageFragment(page, context);
        ((PageFragment)tempFragment).parseViews();
        //todo: async parse
        ((PageFragment)tempFragment).fetchImagesUrls();
    }
    public void replaceFragment(){
        currentFragment = tempFragment;
        FragmentManager manager = getFragmentManager();
        if(manager!=null)
            manager.beginTransaction()
                    .replace(R.id.container, currentFragment)
                    .commit();
    }
    public void changeLanguage(View view){
        Toast.makeText(this,"Be patience, we will add another languages support soon.",Toast.LENGTH_SHORT).show();
        /*
            todo: change languages
            LangLink langLink = page.langlinks.get(1);
            String url = Parser.getApiUrl(langLink.title, langLink.lang);
            setContent(url);
        */
    }
    public void settings(View view){
        //todo: show settings?

        Toast.makeText(this,"Be patience, we will a lot of settings soon.",Toast.LENGTH_SHORT).show();
    }



}
