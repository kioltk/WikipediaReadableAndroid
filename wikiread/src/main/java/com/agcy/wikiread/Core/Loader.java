package com.agcy.wikiread.Core;

import android.os.AsyncTask;
import android.util.Log;

import com.agcy.wikiread.Core.Api.Api;

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
    public abstract void onSuccess(Api response);
    public abstract void onError(Exception exp);
    public abstract void onFinish();
    protected void onPostExecute(Object result) {
        if(result!=null && result instanceof String){
            Api api = null;
            Serializer serializer = new Persister();
            try {
               api = serializer.read(Api.class, (String) result);
            } catch (Exception e) {
                Log.e("wiki","Loader deserialization error");
                e.printStackTrace();
            }

            onSuccess(api);
        }
        else{
            onError((Exception) result);
        }
        onFinish();
    }
}
