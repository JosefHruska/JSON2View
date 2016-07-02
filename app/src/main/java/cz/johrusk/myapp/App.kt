package cz.johrusk.myapp

import android.app.Application

/**
 * Created by Pepa on 02.07.2016.
 */
class App : Application() {

    override fun onCreate() {
        app = this
        super.onCreate()
    }

    companion object {
        private var app: App? = null

        fun get(): App {
            return app as App
        }
    }
}