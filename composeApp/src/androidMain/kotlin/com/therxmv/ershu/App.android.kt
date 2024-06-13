package com.therxmv.ershu

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.therxmv.ershu.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@AndroidApp)
            androidLogger()
            modules(appModule())
        }
    }
}

class AppActivity : ComponentActivity() {

    companion object {
        var ACTIVITY: AppActivity? = null

        fun createIntent(context: Context) = Intent(context, AppActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    override fun onResume() {
        super.onResume()
        ACTIVITY = this
    }

    override fun onPause() {
        super.onPause()
        ACTIVITY = null
    }
}