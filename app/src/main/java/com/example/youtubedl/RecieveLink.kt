package com.example.youtubedl

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebView

class RecieveLink : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recieve_link)

        val intent = intent
        handleSendText(intent)
    }

    private fun handleSendText(intent: Intent){
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        val domain = "http://54.37.64.4/youtubedl/?v="

        /* USE THIS CODE TO OPEN IN BROWSER
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://54.37.64.4/youtubedl/?v=" + sharedText))
        startActivity(intent)
        */

        val webView = findViewById<WebView>(R.id.WebView)
        webView.setDownloadListener{ url, userAgent, contentDescription, mimeType, _ ->
            val request = DownloadManager.Request(Uri.parse(url))
            val cookies = CookieManager.getInstance().getCookie(url)
            request.addRequestHeader("Cookie", cookies)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading Requested File...")
            request.setMimeType(mimeType)
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val fileName = URLUtil.guessFileName(url, contentDescription, mimeType)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            request.setTitle(fileName)
            request.setVisibleInDownloadsUi(true)

            val dManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dManager.enqueue(request)
        }
        webView.loadUrl(domain + sharedText)
    }
}
