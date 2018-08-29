package com.example.youtubedl

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private val writePermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions()
        checkUpdates()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), writePermissionCode)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), writePermissionCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            findViewById<TextView>(R.id.textView).apply { text = R.string.permissionTextGranted.toString() }
        } else {
            findViewById<TextView>(R.id.textView).apply { text = R.string.permissionTextDenied.toString() }
        }
    }

    private fun checkUpdates() {
        doAsync {
            val version = URL("http://54.37.64.4/youtubedl/lastversion.php").readText(Charset.forName("UTF-8"))
            val domain = "http://54.37.64.4/youtubedl/releases/"
            uiThread{
                if (version == BuildConfig.VERSION_NAME){

                }
                else{
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle(R.string.newUpdate)
                            .setMessage("New Version : " + version + "\nAncienne Version : " + BuildConfig.VERSION_NAME)

                    builder.setPositiveButton(R.string.update){_, _ ->
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(domain + "youtubedl.v" + version + ".apk"))
                        startActivity(intent)
                    }

                    builder.setNegativeButton(R.string.skipVersion){_, _ ->

                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

}
