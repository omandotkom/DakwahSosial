package mobile.omandotkom.dakwahsosial.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

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
    private User user = null;

    public RequestMaker(Article article, Context context) {
        this.article = article;
        this.context = context;
    }

    public RequestMaker(User user, Context context) {
        this.context = context;
        this.user = user;
    }

    public void login(LoginListener loginListener) {

        if (user != null) {
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
                    if (user != null) {
                        //TODO : UNCOMMENT THIS LINE
                        user.setJSONResponse(response);
                        user.save();
                        loginListener.onLoginSuccess();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 403) {
                        //Unauthorized
                        Toast.makeText(context, "Username atau Password anda kurang tempat.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Error in network request (" + error.networkResponse.statusCode + ")", Toast.LENGTH_LONG).show();
                    }
                }
            });
            MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }
    }

    UploadStatusListener articleUploadStatusListener;
    public void setArticleUploadStatusListener(UploadStatusListener listener){
        this.articleUploadStatusListener = listener;
    }
    public void upload() {
        if (article != null) {
            //check if there's an image in it
            if (article.getImageMedia() != null && article.getImageMedia().isDisableCheck() == false) {
                //if there's image in it
                ImageUploader imageUploader = new ImageUploader();
                //Proses upload gambar

                imageUploader.uploadMultipart(context, article.getImageMedia().getLocalPath(), new UploadStatusListener() {
                    @Override
                    public void onUploadComplete(String response, int idRequest) {
                        if (idRequest == ImageUploader.IMAGE_UPLOAD_REQUEST_CONST) {
                            //sudah mendapat response
                            Log.d(TAG, "uploading with image in it");
                            article.getImageMedia().setResponse(response);
                            if (article.getDocument()!=null){
                                ImageUploader fileUploader = new ImageUploader();
                                fileUploader.uploadMultipart(context, article.getDocument().getLocalPath(), new UploadStatusListener() {
                                    @Override
                                    public void onUploadComplete(String response, int idRequest) {
                                        article.getDocument().setResponse(response);
                                        compose();
                                    }
                                });
                            }else {
                                compose();
                            }
                        }
                    }
                });

            }
            else if (article.getImageMedia()==null){
                if (article.getDocument()!=null){
                    ImageUploader fileUploader = new ImageUploader();
                    fileUploader.uploadMultipart(context, article.getDocument().getLocalPath(), new UploadStatusListener() {
                        @Override
                        public void onUploadComplete(String response, int idRequest) {
                            article.getDocument().setResponse(response);
                            compose();
                        }
                    });
                }
            }
            else {
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
                articleUploadStatusListener.onUploadComplete(null,300);
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
                headers.put("Authorization", "Bearer " + new User(context).getToken());
                return headers;
            }
        };

        MySingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);


    }

}
