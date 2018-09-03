package mobile.omandotkom.dakwahsosial.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import mobile.omandotkom.dakwahsosial.MainActivity;
import mobile.omandotkom.dakwahsosial.R;
import mobile.omandotkom.dakwahsosial.URLS;
import mobile.omandotkom.dakwahsosial.data.User;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BrowserActivity extends AppCompatActivity {
    private WebView mWebView;
    private Button button;
    //private final String URL = "http://192.168.43.32/dakwah";
    private String TAG = "TAG_BROWSER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String URL = getIntent().getStringExtra("page_url");
        if (URL == null || URL.isEmpty()) {
            URL = URLS.SERVER_URL;
        }

        setContentView(R.layout.activity_browser);
        button = (Button) findViewById(R.id.create);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new mobile.omandotkom.dakwahsosial.webview.WebViewClient());
        mWebView.loadUrl(URL);
        //TODO : REMOVE THESE SHARED PREFERECES LINE
        User user = new User(getApplicationContext());
        Log.d(TAG, String.valueOf(user.isLoggedIn()));


        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Berhasil Subscribe";
                        if (!task.isSuccessful()) {
                            msg = "Gagal Subscribe";
                        }
                        Log.d(TAG, msg);
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(getApplicationContext());
                if (user.isLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("command", "compose");
                    startActivity(intent);
                } else {
                    //belum login
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("command", "LOGIN_REQUIRED");
                    startActivity(intent);
                }

            }
        }
    );

}

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
