package com.kings.yatharth.yatharthresume.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by yatharth on 21/06/17.
 */

public class HttpRequestAsynTask extends AsyncTask<String, Void, String> {

    private final HttpClient httpClient = new DefaultHttpClient();
    private String content = null;
    private Context context;
    private AsyncTaskCompleteListener<String> callback;

    public HttpRequestAsynTask(Context context, AsyncTaskCompleteListener<String> callback)
    {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        try
        {
            //urls[0] = URLEncoder.encode(urls[0], "UTF-8");

            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            content = httpClient.execute(httpget, responseHandler);
        }
        catch (ClientProtocolException e)
        {
            //error = e.getMessage();
            cancel(true);
        }
        catch (IOException e)
        {
            //error = e.getMessage();
            cancel(true);
        }

        httpClient.getConnectionManager().shutdown();

        return content;
    }

    protected void onPostExecute(String result)
    {
        System.out.println("on Post execute called");
        callback.onTaskComplete(result);
    }
}

