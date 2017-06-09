package com.st33fo.glideforktt;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Stefan on 8/7/2016.
 */


public class GetDocument {
    private Context context;
    private String sessionId;
    private static RequestQueue myRequestQueue = null;

    public GetDocument(Context context){
        this.context=context;
        sessionId = SecuredSharePreference.getPrefCookies(context);
    }

    public Document GetDocument(String site) throws Exception {
        final Document[] doc = new Document[1];
        final CountDownLatch cdl = new CountDownLatch(1);


        StringRequest documentRequest = new StringRequest( //
                Request.Method.GET, //
                site, //
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        doc[0] = Jsoup.parse(response);

                        cdl.countDown();
                    }
                }, //

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Houston we have a problem ... !");
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
                params.put("Cookie", "PHPSESSID=" + sessionId);
                params.put("Content-Type","application/json; charset=utf-8");


                return params;
            }

        };
        documentRequest.setRetryPolicy(new DefaultRetryPolicy( 10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (myRequestQueue == null) {
            myRequestQueue = Volley.newRequestQueue(context);
        }

        // Add the request to the queue...

        myRequestQueue.add(documentRequest);

        // ... and wait for the document.
        // NOTA: Be aware of user experience here. We don't want to freeze the app...
        cdl.await();

        return doc[0];
    }
    }

