package com.jdy.android.fortube

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.jdy.android.fortube.map.MapViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    private val mMapViewModel: MapViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map_view.setMapViewModel(mMapViewModel)

        disableAppbarBehavior()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
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

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}