package umg.edu.gt.progra2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import umg.edu.gt.progra2.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGastos.setOnClickListener {
            startActivity(Intent(this, GastoActivity::class.java))
        }

        binding.btnIngresos.setOnClickListener {
            startActivity(Intent(this, IngresoActivity::class.java))
        }

        binding.btnBalance.setOnClickListener {
            startActivity(Intent(this, BalanceActivity::class.java))
        }

        binding.btnPerfilUsuario.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(Prefs.MODO_EDICION, true)
            }
            startActivity(intent)
        }
    }
    
    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences(Prefs.ARCHIVO, MODE_PRIVATE)
        val nombre = sharedPreferences.getString(Prefs.NOMBRE, null)
            ?: intent.getStringExtra(Prefs.NOMBRE)
            ?: "Usuario"
        binding.tvNombreUsuario.text = nombre
    }
}