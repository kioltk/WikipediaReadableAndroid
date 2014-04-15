package com.agcy.wikiread.Core;

import android.os.AsyncTask;
import android.util.Log;

import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Core.Api.SearchSuggestion;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Created by kiolt_000 on 27.02.14.
 */
public abstract class Loader extends AsyncTask<String, Void, Object> {

    public static final int PAGE = 0;
    public static final int RANDOM = 0;
    public static final int SEARCH = 1;
    int loadingType;
    public Loader(int loadingType){
        this.loadingType = loadingType;
    }
    @Override
    protected Object doInBackground(String... urls) {
        String responseStr = null;

        try {
            for (String url : urls) {
                url = url.replace(" ","%20");
                url = url.replace("|","%7C");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(get);
                HttpEntity httpEntity = httpResponse.getEntity();
                responseStr = EntityUtils.toString(httpEntity);
            }
        } catch (Exception exp){
            Log.e("wiki","loader error");

            return exp;
        }
        return responseStr;
    }
    public abstract void onSuccess(Object response);
    public abstract void onError(Exception exp);
    public abstract void onFinish();
    protected void onPostExecute(Object result) {
        if(result!=null && result instanceof String){
            Object response = null;
            Serializer serializer = new Persister();
            try {

                Class<?> responseClass = Api.class;
                if(loadingType==SEARCH)
                    responseClass = SearchSuggestion.class;
                response = serializer.read(responseClass, (String) result);
            } catch (Exception e) {
                Log.e("wiki","Loader deserialization error");
                e.printStackTrace();
            }

            onSuccess(response);
        }
        else{
            onError((Exception) result);
        }
        onFinish();
    }
}
