package umg.edu.gt.progra2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import umg.edu.gt.progra2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Propiedad de la clase (no local a onCreate) para poder usarla también en irAlMenu
    private var esEdicion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(Prefs.ARCHIVO, MODE_PRIVATE)

        val nombreGuardado = sharedPreferences.getString(Prefs.NOMBRE, null)
        val correoGuardado = sharedPreferences.getString(Prefs.CORREO, null)

        esEdicion = intent.getBooleanExtra(Prefs.MODO_EDICION, false)
        // Si ya hay un usuario pasa directamente al menú y no muestra esta pantalla
        if (nombreGuardado != null && !esEdicion) {
            irAlMenu(nombreGuardado)
            return
        }

        // Inicializa la interfaz antes de acceder a sus elementos
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (esEdicion) {
            binding.etNombre.setText(nombreGuardado ?: "")
            binding.etCorreo.setText(correoGuardado ?: "")
            binding.tvTitulo.text = "Editar Perfil"
            binding.btnGuardar.text = "Guardar Cambios"
        }

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            //Se valida el nombre
            if (nombre.isEmpty()) {
                mostrarAviso("El nombre no puede estar vacío.")
                return@setOnClickListener
            }

            if (correo.isEmpty()) {
                mostrarAviso("El correo no puede estar vacío.")
                return@setOnClickListener
            }
            // Se valida el correo
            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                mostrarAviso("El correo no es válido.")
                return@setOnClickListener
            }

            with(sharedPreferences.edit()) {
                putString(Prefs.NOMBRE, nombre)
                putString(Prefs.CORREO, correo)
                apply()
            }

            mostrarAviso("¡Datos guardados con éxito!")
            irAlMenu(nombre)
        }
    }

    private fun mostrarAviso(mensaje: String) =
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

    private fun irAlMenu(nombre: String) {
        if (esEdicion) {
            // Ya había una MenuActivity debajo en la pila; no hace falta crear otra
            finish()
            return
        }
        val intent = Intent(this, MenuActivity::class.java).apply {
            putExtra(Prefs.NOMBRE, nombre)
        }
        startActivity(intent)
        finish()
    }
}