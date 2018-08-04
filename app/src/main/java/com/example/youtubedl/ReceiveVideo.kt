package com.example.youtubedl

import android.app.AlertDialog
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

class ReceiveVideo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_video)

        val intent = intent
        handleSendText(intent)
    }

    private fun handleSendText(intent: Intent){
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        var url: String
        var opt: String

        if(sharedText.startsWith("https://youtu.be/")){
            //Youtube just share url without text addition
            url = sharedText
            opt = ""

            val builder = AlertDialog.Builder(this)
            builder.setTitle("YouTube DL")
                    .setMessage(R.string.dialogMessage)

            builder.setPositiveButton("Video"){dialog, which ->
                opt = "ytv"
                download(url, opt)
            }

            // Display a negative button on alert dialog
            builder.setNegativeButton("Audio"){dialog, which ->
                opt = "yta"
                download(url, opt)
            }

            val dialog = builder.create()
            dialog.show()

        }
        else if (sharedText.contains("https://twitter.com/")){
            //Just keeping the url in Twitter shared text
            url = sharedText.substring(sharedText.lastIndexOf("https"))
            opt = "twv"
            download(url, opt)
        }
    }

    private fun download(url: String, opt: String){
        val webView = findViewById<WebView>(R.id.WebView)
        val domain = "http://54.37.64.4/youtubedl/video.php?v="
        val suffix = ("&o=")
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
        webView.loadUrl(domain + url + suffix + opt)

        /* USE THIS CODE TO OPEN IN BROWSER
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(domain + url + suffix + opt))
        startActivity(intent)
        */
    }
}
