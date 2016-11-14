package com.liborgsadmin.Datahelpers;

import com.liborgsadmin.interfaces.ResponseInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sakshiagarwal on 10/11/16.
 */

class NetworkHelper {

    void postRequestForServer(String url, RequestParams params, String auth_token, final ResponseInterface responseInterface)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("auth-token", auth_token);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONObject object) {
                responseInterface.onSuccess(object);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject message) {
                responseInterface.onFailure(message.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseInterface.onFailure(responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                responseInterface.onFailure(errorResponse.toString());
            }
        });
    }

    void getRequestForServer(String url, String auth_token, final ResponseInterface responseInterface)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("auth-token", auth_token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                responseInterface.onSuccess(object);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String mString, Throwable e) {
                responseInterface.onFailure(mString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                responseInterface.onFailure(errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                responseInterface.onFailure(errorResponse.toString());
            }
        });
    }
}
