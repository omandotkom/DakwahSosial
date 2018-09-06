package mobile.omandotkom.dakwahsosial.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private final String SHARED_PREF_FILE_KEY = "mobile.omandotkom.dakwahsosial.data.USER";
    private final String SP_LOGGED_IN_STATUS = "isloggedin";
    private final String SP_TOKEN = "token";
    private final String SP_DISPLAY_NAME = "user_display_name";
    private String username, password, token;
    private String nice_name, display_name;
    private boolean loggedIn = false;
    private Context context;
    private String TAG = "USER_CLASS";
    private JSONObject JSONResponse;
    private SharedPreferences sharedPreferences;

    public User(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE_KEY, Context.MODE_PRIVATE);
    }

    public JSONObject getJSONResponse() {
        return JSONResponse;
    }

    public void setJSONResponse(JSONObject JSONResponse) {
        this.JSONResponse = JSONResponse;
        parseJSON();
    }

    private void parseJSON() {
        try {
            token = JSONResponse.getString("token");
            display_name = JSONResponse.getString("user_display_name");
            nice_name = JSONResponse.getString("user_nicename");
            loggedIn = true;
        } catch (JSONException jsoe) {
            Log.e(TAG, jsoe.getMessage());
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        token = sharedPreferences.getString(SP_TOKEN, "Unknown");
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNice_name() {
        nice_name = sharedPreferences.getString(SP_DISPLAY_NAME, "Unknown");

        return nice_name;
    }

    public void setNice_name(String nice_name) {
        this.nice_name = nice_name;
    }

    public String getDisplay_name() {
        display_name = sharedPreferences.getString(SP_DISPLAY_NAME, "Unknown");
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public boolean isLoggedIn() {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(SP_LOGGED_IN_STATUS, false);
        } else {
            return false;
        }
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_TOKEN, token);
        editor.putBoolean(SP_LOGGED_IN_STATUS, loggedIn);
        editor.putString(SP_DISPLAY_NAME, display_name);
        editor.apply();
        Log.d(TAG, "Succesfully save " + getDisplay_name());
    }

}
