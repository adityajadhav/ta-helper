package com.cs442.team4.tahelper.messages;

import android.content.Context;
import android.util.Log;

import com.cs442.team4.tahelper.contants.ApplicationConstants;
import com.cs442.team4.tahelper.model.PushNotification;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by neo on 23-11-2016.
 */

public class Message {

    private static String TAG = Message.class.getName();

    public static void notify(Context context, PushNotification notification) {
        Gson gson = new Gson();
        StringEntity entity = new StringEntity(gson.toJson(notification), ContentType.APPLICATION_JSON);
        post(context, entity);
    }

    private static void post(Context context, StringEntity entity) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", ApplicationConstants.SERVER.API_KEY);
        client.post(context, ApplicationConstants.SERVER.CLOUD_MESSAGING_URI, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "onSuccess: notified ");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onSuccess: notified ");
            }
        });
    }

}
