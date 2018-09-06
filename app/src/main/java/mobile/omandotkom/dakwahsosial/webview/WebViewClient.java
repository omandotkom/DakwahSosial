package mobile.omandotkom.dakwahsosial.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by omandotkom on 14/08/18.
 */

public class WebViewClient extends android.webkit.WebViewClient {
    private final String TAG = "WEBVIEWCLIENT";
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d(TAG,"Loading page for " + url);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    Log.d(TAG,"Loading complete.");
    }

}
