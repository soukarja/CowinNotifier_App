package com.cowinnotifier.alertsappforcowinindia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

public class inbuiltWebviewActivity extends AppCompatActivity {
    private WebView webView;
    private String siteLink;

    private  adsManager ads;
    private LinearLayout bannerBox;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbuilt_webview);

        Intent intent = getIntent();
        siteLink = intent.getStringExtra("link");
        String header = intent.getStringExtra("header");

        ads = new adsManager(inbuiltWebviewActivity.this, false);

        bannerBox = (LinearLayout) findViewById(R.id.BannerBox);


        ((TextView)findViewById(R.id.header_title)).setText(header);
        ((TextView) findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.refreshBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
//                progressDialog.show();
                ads.showInterstitialAds();
            }
        });

//        progressDialog = new ProgressDialog(inbuiltWebviewActivity.this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new HelloWebViewClient());
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(siteLink);
    }

    public class HelloWebViewClient extends WebViewClient
    {
        public HelloWebViewClient()
        {
            // do nothing
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (webView.canGoBack())
            webView.goBack();
        else
            finish();
    }
}