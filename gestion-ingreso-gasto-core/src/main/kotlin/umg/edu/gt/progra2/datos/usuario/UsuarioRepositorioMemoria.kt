package umg.edu.gt.progra2.datos.usuario

import umg.edu.gt.progra2.modelo.Usuario

class UsuarioRepositorioMemoria : UsuarioRepositorio {
    private var usuarioUnico: Usuario? = null

    override suspend fun guardar(usuario: Usuario) {
        usuarioUnico = usuario
    }

    override suspend fun obtener(): Usuario? = usuarioUnico
}