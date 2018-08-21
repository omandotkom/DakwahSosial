package mobile.omandotkom.dakwahsosial.webview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.messaging.FirebaseMessaging;

import mobile.omandotkom.dakwahsosial.MainActivity;
import mobile.omandotkom.dakwahsosial.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BrowserActivity extends Activity {
    private WebView mWebView;
//private final String URL = "http://192.168.43.32/dakwah";
private String TAG = "TAG_BROWSER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String URL = getIntent().getStringExtra("page_url");
        setContentView(R.layout.activity_browser);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new mobile.omandotkom.dakwahsosial.webview.WebViewClient());
        mWebView.loadUrl(URL);

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

    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
