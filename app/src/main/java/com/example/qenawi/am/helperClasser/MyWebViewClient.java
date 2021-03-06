package com.example.qenawi.am.helperClasser;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * Created by QEnawi on 4/17/2017.
 */

public class MyWebViewClient extends WebViewClient {

    // show the web page in webview but not in web browser
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);

    }

}