package umg.edu.gt.progra2

import android.app.Application
import umg.edu.gt.progra2.ui.Dependencias

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Dependencias.iniciar(this)
    }
}