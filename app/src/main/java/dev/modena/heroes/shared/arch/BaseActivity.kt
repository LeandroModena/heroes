package dev.modena.heroes.shared.arch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

abstract class BaseActivity : ComponentActivity(), DefaultLifecycleObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super<ComponentActivity>.onCreate(savedInstanceState)
        lifecycle.addObserver(this)
        init()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        lifecycle.removeObserver(this)
        super<DefaultLifecycleObserver>.onDestroy(owner)
    }

    abstract fun init()

}