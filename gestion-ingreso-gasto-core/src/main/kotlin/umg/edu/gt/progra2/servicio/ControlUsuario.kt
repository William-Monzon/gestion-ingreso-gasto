package umg.edu.gt.progra2.servicio

import umg.edu.gt.progra2.datos.usuario.UsuarioRepositorio
import umg.edu.gt.progra2.modelo.Usuario

class ControlUsuario(private val repositorio: UsuarioRepositorio) {

    fun validar(usuario: Usuario): String? = when {
        usuario.nombre.isBlank() -> "El nombre no puede estar vacío"
        !usuario.correo.contains("@") || !usuario.correo.contains(".") -> "Ingrese un correo válido"
        else -> null
    }

    suspend fun registrarPerfilInicial(usuario: Usuario): String? {
        validar(usuario)?.let { return it }
        repositorio.guardar(usuario)
        return null
    }

    suspend fun obtenerPerfil(): Usuario? = repositorio.obtener()
}