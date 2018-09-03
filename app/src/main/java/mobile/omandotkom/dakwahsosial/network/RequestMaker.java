package mobile.omandotkom.dakwahsosial.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mobile.omandotkom.dakwahsosial.URLS;
import mobile.omandotkom.dakwahsosial.converter.TextToHtml;
import mobile.omandotkom.dakwahsosial.data.Article;
import mobile.omandotkom.dakwahsosial.data.User;

public class RequestMaker {
    private final String TAG = "JSONREQUEST";
    private Article article = null;
    private Context context;
    private User user=null;

    public RequestMaker(Article article, Context context) {
        this.article = article;
        this.context = context;
    }

    public RequestMaker(User user){
        this.user = user;
    }
    public void login(){

        if (user!=null){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", user.getUsername());
                jsonObject.put("password", user.getPassword());
            } catch (JSONException jspoe) {
                Log.e(TAG, jspoe.getMessage());
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLS.AUTH_URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG,response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG,error.getMessage());
                }
            });
                    MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }}


    public void upload() {
        if (article != null) {
            //check if there's an image in it
            if (article.getImageMedia() != null && article.getImageMedia().isDisableCheck() == false) {
                //if there's image in it
                ImageUploader imageUploader = new ImageUploader();
                //Proses upload gambar
                imageUploader.uploadMultipart(context, article.getImageMedia().getLocalPath(), new UploadStatusListener() {
                    @Override
                    public void onImageUploadComplete(String response) {
                        //sudah mendapat response
                        Log.d(TAG, "uploading with image in it");
                        article.getImageMedia().setResponse(response);

                        compose();
                    }
                });
            } else {
                compose();
                Log.d(TAG, "uploading without image in it");
            }
        }
    }

    private void sendNotification() {

        StringRequest request = new StringRequest(Request.Method.POST, URLS.NOTIFICATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (article != null && article.isErrorThrown() == false) {
                    params.put("message", article.getLink());
                    params.put("title", article.getTitle());
                }
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void compose() {
        //TODO : COMPOSE THE CONTENT TO MAKE IT READY FOR UPLOAD
        //fill the html part
        article.setHtml(TextToHtml.getHtml(article));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", article.getTitle());
            jsonObject.put("content", article.getHtml());
            jsonObject.put("status", article.getSTATUS());
            jsonObject.put("excerpt", article.getEXCERPT());
        } catch (JSONException jspoe) {
            Log.e(TAG, jspoe.getMessage());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLS.UPLOAD_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                article = new Article();
                article.setJSONResponse(response.toString());
                sendNotification();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof AuthFailureError) {
                            Log.e(TAG, "MASUK KESINI");
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic b21hbmRvdGtvbTpzeXN0ZW0zMjk4");
                return headers;
            }
        };

        MySingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);


    }

}
