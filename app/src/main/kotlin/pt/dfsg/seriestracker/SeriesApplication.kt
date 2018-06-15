package pt.dfsg.seriestracker

import android.app.Application
import pt.dfsg.seriestracker.di.*
import timber.log.Timber


class SeriesApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
        initializeTimber()

    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .okHttpClientModule(OkHttpClientModule())
            .roomModule(RoomModule())
            .remoteModule(RemoteModule())
            .build()
    }

    private fun initializeTimber() {
        Timber.uprootAll()
        Timber.plant(Timber.DebugTree())
    }

//    private fun initializeSonar() {
//        if (BuildConfig.DEBUG && SonarUtils.shouldEnableSonar(this)) {
//            val client = AndroidSonarClient.getInstance(this)
//            client.addPlugin(InspectorSonarPlugin(this, DescriptorMapping.withDefaults()))
//            client.addPlugin(NetworkSonarPlugin())
//            client.start()
//
//        }
//    }

}