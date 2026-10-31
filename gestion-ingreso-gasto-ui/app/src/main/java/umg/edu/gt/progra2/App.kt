package umg.edu.gt.progra2

import android.app.Application
import umg.edu.gt.progra2.ui.Dependencias

// Esta es lo primero que habré la app e inicializa las dependencias
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Dependencias.iniciar(this)
    }
}