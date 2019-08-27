package com.jdy.android.fortube

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.jdy.android.fortube.base.PrefHelper
import com.jdy.android.fortube.map.MapViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    companion object {
        const val REQ_LOCATION_PERMISSION = 1512
        const val REQ_APP_SETTINGS = 1513
    }
    private val mMapViewModel: MapViewModel by viewModel()
    private val mPrefHelper: PrefHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map_view.setMapViewModel(mMapViewModel)

        observeMapData()
        disableAppbarBehavior()

        if (checkLocationPermissions()) {
            map_view.setCurrentLocation()
        }
    }

    private fun observeMapData() {
        mMapViewModel.mapData.observe(this, Observer { map ->
            map_view.addMarkerList(map.documents)
        })
    }

    private fun disableAppbarBehavior() {
        appbar.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val appbarBehavior = (appbar.layoutParams as CoordinatorLayout.LayoutParams).behavior
                (appbarBehavior as AppBarLayout.Behavior).run {
                    setDragCallback(object: AppBarLayout.Behavior.DragCallback() {
                        override fun canDrag(appBarLayout: AppBarLayout) = false
                    })
                }
                appbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun checkLocationPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (mPrefHelper.isDeniedShowPermissionPopup()) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permission_location_title))
                    .setMessage(getString(R.string.permission_location_message))
                    .setPositiveButton(getString(R.string.btn_go_setting)) { dialog, _ ->
                        startActivityForResult(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", packageName, null)
                        }, REQ_APP_SETTINGS)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQ_LOCATION_PERMISSION)
            }
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQ_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map_view.setCurrentLocation()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                mPrefHelper.denyShowPermissionPopup()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_APP_SETTINGS) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map_view.setCurrentLocation()
            }
        }
    }
}