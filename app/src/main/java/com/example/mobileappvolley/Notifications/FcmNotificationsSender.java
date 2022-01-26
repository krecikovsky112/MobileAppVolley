package com.example.mobileappvolley.Notifications;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileappvolley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {
    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAABaHGHnc:APA91bH6YlFFKaWBrEazjPZ4qg3rXaoW2sXsxjfL-wS-ZA7kbcr2LZnkTvm9VzuZ5xyOTu58edZYdoxFunnQvDyVkTfZw0rUZeUW1dopQuYx4G4C4x3bJYdxEoxsD9ZNSx8-LLCyxTyt";

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void SendNotifications() {
        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", R.drawable.icon); // enter icon that exists in drawable only

            mainObj.put("notification", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response -> {

                // code run is got response

            }, error -> {
                // code run is got error

            }) {
                @Override
                public Map<String, String> getHeaders() {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;
                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
