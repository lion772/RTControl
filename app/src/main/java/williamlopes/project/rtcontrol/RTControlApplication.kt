package williamlopes.project.rtcontrol

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import williamlopes.project.rtcontrol.di.Module

class RTControlApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RTControlApplication)
            modules(Module.moduleDI)
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }
}