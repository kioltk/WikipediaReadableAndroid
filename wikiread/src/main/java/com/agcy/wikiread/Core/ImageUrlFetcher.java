package com.agcy.wikiread.Core;

import android.os.AsyncTask;
import android.util.Log;

import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Models.Image;
import com.agcy.wikiread.Models.Page;
import com.agcy.wikiread.Views.PictureView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kiolt_000 on 08.03.14.
 */
public abstract class ImageUrlFetcher extends AsyncTask<Void, Void, String> {
    //http://en.wikipedia.org/w/api.php?action=query&titles=#TITLES#&prop=imageinfo&iiprop=url|sha1
    public HashMap<String,PictureView> listeners;
    ArrayList<Image> images;
    public ImageUrlFetcher(ArrayList<PictureView> listeners){
        this.listeners = new HashMap<String, PictureView>();
        this.images = new ArrayList<Image>();
        for(PictureView listener:listeners){
            addListenerView(listener);
            images.add(listener.getImage());
        }

    }

    @Override
    protected String doInBackground(Void... params) {

        int countOfCycles = images.size()/50+1;
        if(images.isEmpty())
            countOfCycles = 0;
        for(int cycle=0; cycle<countOfCycles; cycle++){

            String names = "";
            int countOfImages = 50;
            if(cycle==countOfCycles-1){
                countOfImages = images.size() % 50;
            }
            for (int index =cycle*50; index<cycle*50+countOfImages; index++) {
                names += images.get(index).title + "|";
            }
            names = names.substring(0, names.length()-1);
            try {
                //todo: langs
                //names = names.replace(".","%2E");
                names = names.replace("&","%26");
                String url = "http://en.wikipedia.org/w/api.php?action=query&format=xml&prop=imageinfo&iiprop=url&iilimit=500&titles=" + names;
                url = url.replace(" ","%20");
                url = url.replace("|","%7C");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(get);
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);

                Api api = null;
                Serializer serializer = new Persister();
                try {
                    api = serializer.read(Api.class, result);
                    parseImageResponse(api);

                    Log.i("wiki", "serialization success");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (Exception exp){
                Log.e("wiki"," "+exp.getLocalizedMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        onFetched();
    }

    public void parseImageResponse(Api response){

        for(Page page:response.query.pages){
            Image image = page.imageinfo.get(0);
            PictureView listener = listeners.get(page.title);
            if(listener!=null){
                listener.getImage().url = image.url;
                listeners.remove(listener.getImageName());
            }
        }

        Log.i("wiki"," количество картинок: " + listeners.size());

    }
    public void addListenerView(PictureView pictureView) {
        listeners.put(pictureView.getImageName(), pictureView);
    }
    public abstract void onFetched();
}
