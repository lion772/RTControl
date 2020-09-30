package williamlopes.project.rtcontrol.helper

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

object UsuarioFirebase {

    val usuarioAtual: FirebaseUser?
        get() {
            val usuario = ConfiguracaoFirebase.firebaseAutenticacao
            return usuario?.currentUser
        }

    fun getIdUsuario(): String?{
        return usuarioAtual?.uid
    }


    fun atualizarNomeUsuario(nome: String?): Boolean {
        return try {
            //Usuario logado no app
            val usuarioLogado = usuarioAtual
            //Configurar objeto para alteração do perfil
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build()
            usuarioLogado?.updateProfile(profile)
                ?.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("Perfil", "Erro ao atualizar nome do perfil")
                    }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun atualizarTipoUsuario(tipo: String?): Boolean {
        return try {
            val user = usuarioAtual
            //Configurar objeto para alteração do perfil
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(tipo)
                .build()
            user?.updateProfile(profile)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun atualizarFotoUsuario(url: Uri?) {
        try {
            //Usuario logado no app
            val usuarioLogado = usuarioAtual
            //Configurar objeto para alteração do perfil
            val profile = UserProfileChangeRequest.Builder()
                .setPhotoUri(url)
                .build()
            usuarioLogado!!.updateProfile(profile)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("Perfil", "Erro ao atualizar a foto do perfil")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*val dadosUsuarioLogado: User
        get() {
            val firebaseUser = usuarioAtual
            val user = User("","","", "", "")
            user.name(firebaseUser?.displayName)
            firebaseUser?.uid?.let { user.id(it) }
            if (firebaseUser.photoUrl == null) {
                user.urlImage = ""
            } else {
                user.urlImage = firebaseUser?.photoUrl.toString()
            }
            return user
        }*/
}


