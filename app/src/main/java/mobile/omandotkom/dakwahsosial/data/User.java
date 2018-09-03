package mobile.omandotkom.dakwahsosial.data;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private String username,password,token;
    private String nice_name,display_name;
    private boolean isLoggedIn = false;
    private Context context;
    private String TAG = "USER_CLASS";



    private final String SHARED_PREF_FILE_KEY = "mobile.omandotkom.dakwahsosial.data.USER";
    private final String SP_LOGGED_IN_STATUS = "isloggedin";
    private final String SP_TOKEN = "token";
    private final String SP_USERNAME = "username";
    private final String SP_DISPLAY_NAME = "display_name";
    private SharedPreferences sharedPreferences;
    public User(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE_KEY,Context.MODE_PRIVATE);
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
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNice_name() {
        return nice_name;
    }

    public void setNice_name(String nice_name) {
        this.nice_name = nice_name;
    }

    public String getDisplay_name() {
        display_name = sharedPreferences.getString(SP_DISPLAY_NAME,"Unknown");
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public boolean isLoggedIn() {
         if (sharedPreferences!=null){
            return sharedPreferences.getBoolean(SP_LOGGED_IN_STATUS,false);
        }else{return false;}
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void save(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_TOKEN,getToken());
        editor.putBoolean(SP_LOGGED_IN_STATUS,true);
        editor.putString(SP_USERNAME,getUsername());
        editor.putString(SP_DISPLAY_NAME,getDisplay_name());
        editor.apply();
    }

}
