package com.yseleshi.stockinfo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by dell laptop on 8/16/2017.
 */

public class VolleyClassString {

    private final String TAG = "VolleyClassString";
    private RequestQueue queue;
    private Context mContext;
    private  OnInfoListener onInfoListener;

    public VolleyClassString(Context context, OnInfoListener<String> callback) {
        mContext = context;
        onInfoListener = callback;
    }

    public interface OnInfoListener<String> {
        public void onInfoAvailable(String responseString);
    }

    public synchronized RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(mContext);

        }
        return queue;
    }

    public void makeNetworkRequests(String query){


        String queryString = query;
        queue = getRequestQueue();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, queryString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Log.d(TAG, "onResponse() called response: "
                                    + (response != null?response.toString():"null"));

                            if (onInfoListener != null)
                                onInfoListener.onInfoAvailable(response);
                        }
                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error response ->" + error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


}
