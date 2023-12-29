package com.example.task

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewDatabase
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class HomeActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
     lateinit var  webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        supportActionBar?.apply {
            title = "Home"
            // Other customizations like setting a subtitle, adjusting text color, etc.

        }

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar)
        webView= findViewById(R.id.webview)

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true;
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.loadsImagesAutomatically = true;
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
//        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

//        webSettings.saveFormData = false;
//        webSettings.databaseEnabled = false;

//        webView.clearFormData();
//        webView.clearHistory();
//        webView.clearCache(true);
//        WebViewDatabase.getInstance(this).clearUsernamePassword();
//        WebStorage.getInstance().deleteAllData();
//        val cookies = CookieManager.getInstance().getCookie("https://user-uat.api-hkbgaming888.com/")
//        val localStorage = webView.evaluateJavascript("localStorage.getItem('your_key');") { value ->
//            Log.d("webviewerror",value)
//            //Log.d("webviewerrorcookies",cookies)
//        }
//        Log.d("webviewerrorlocalStorage", localStorage.toString())
        val webViewClient = WebViewClientImpl(this)
        webView.webViewClient = webViewClient
//        CookieManager.getInstance().setAcceptCookie(false);
        webView.clearCache(true)
        webView.loadUrl("https://user-uat.api-hkbgaming888.com/")

    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack()) {
            this.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }



}