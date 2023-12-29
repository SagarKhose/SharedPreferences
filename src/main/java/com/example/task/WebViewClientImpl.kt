package com.example.task

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebViewDatabase


class WebViewClientImpl(private val activity: Activity) : WebViewClient() {

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
//        if (url.contains("https://www.youtube.com/")) {
//            return false
//        }
//        Log.d("WebView",url)
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//        activity.startActivity(intent)
//        webView.clearFormData();
//        webView.clearHistory();
//        webView.clearCache(true);
//        CookieManager.getInstance().setAcceptCookie(false);
    WebStorage.getInstance().deleteAllData()
        webView.clearCache(true)
        webView.loadUrl(url)
        webView.reload()
        return true
    }

}