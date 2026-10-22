package umg.edu.gt.progra2.datos.usuario

import umg.edu.gt.progra2.modelo.Usuario

interface UsuarioRepositorio {
    suspend fun guardar(usuario: Usuario)
    suspend fun obtener(): Usuario?
}