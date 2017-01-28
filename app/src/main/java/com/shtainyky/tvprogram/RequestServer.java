package com.shtainyky.tvprogram;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shtainyky.tvprogram.parser.Parse;

public class RequestServer {
    public static String requestData(final Context context, String uri) {
        final String[] result = new String[1];
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result[0] = Parse.parseJSONtoString(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        result[0] = "no result";
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
        return result[0];

    }
}
