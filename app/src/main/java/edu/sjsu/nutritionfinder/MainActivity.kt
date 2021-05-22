package edu.sjsu.nutritionfinder

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    lateinit var appbarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        var navController = findNavController(R.id.main_content)
        appbarConfig = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appbarConfig)

        if(!hasCameraPermission()){
            requestCameraPermission()
        }

        notifyUser()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_content)
        return navController.navigateUp(appbarConfig)
                || super.onSupportNavigateUp()
    }

    private fun hasCameraPermission() : Boolean{
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 1)
    }

    private fun notifyUser() {
        var alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Nutrition Finder")
        alertBuilder.setMessage(resources.getString(R.string.intro))
        alertBuilder.setPositiveButton("Ok"
        ) { p0, _ -> p0?.dismiss() }
        alertBuilder.create().show()
    }
}