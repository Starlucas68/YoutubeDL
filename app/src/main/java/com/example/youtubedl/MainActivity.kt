package com.example.youtubedl

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.google.android.gms.ads.MobileAds;


class MainActivity : AppCompatActivity() {

    private val writePermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions()
        MobileAds.initialize(this, "ca-app-pub-9970155051487841~9543640699")
    }

    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permission != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), writePermissionCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            findViewById<TextView>(R.id.textView).apply { text = R.string.permissionTextGranted.toString() }
        }
        else{
            findViewById<TextView>(R.id.textView).apply { text = R.string.permissionTextDenied.toString() }
        }
    }

}
