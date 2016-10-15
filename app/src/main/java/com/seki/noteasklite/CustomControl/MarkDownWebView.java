package com.seki.noteasklite.CustomControl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by yuan on 2016/5/26.
 */
public class MarkDownWebView extends WebView {
    public MarkDownWebView(Context context) {
        super(context);
    }

    public MarkDownWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkDownWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MarkDownWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MarkDownWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    public void ini() {
        this.setWebViewClient(new MyWebView(getContext()));
        this.getSettings().setDefaultTextEncodingName("UTF-8");
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.getSettings().setJavaScriptEnabled(true);
    }
    public static class MyWebView extends WebViewClient {
        Context context=null;
        public MyWebView(Context context){
            this.context=context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url); //url为你要链接的地址
            Intent intent =new Intent(Intent.ACTION_VIEW, uri);
            ((Activity)context).startActivity(intent);
            return true;
        }
    }
}
