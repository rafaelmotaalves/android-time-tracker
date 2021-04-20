package br.ufpe.cin.timetracker.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsHelper (private val activity: Activity) {

    companion object {
        val USED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun hasPermissions() = USED_PERMISSIONS.all {
        hasPermission(it)
    }

    private fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    fun requestPermissions() =
        ActivityCompat.requestPermissions(activity, USED_PERMISSIONS, 1)

}