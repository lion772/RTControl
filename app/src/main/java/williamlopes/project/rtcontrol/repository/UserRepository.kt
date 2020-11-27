package williamlopes.project.rtcontrol.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.util.Constants
import williamlopes.project.rtcontrol.util.Constants.DEFAULT_UPLOAD
import williamlopes.project.rtcontrol.util.Constants.IMAGE
import williamlopes.project.rtcontrol.util.Constants.SLACK
import williamlopes.project.rtcontrol.util.Constants.SUCCESS
import williamlopes.project.rtcontrol.util.await
import java.net.URI


@ExperimentalCoroutinesApi
class UserRepository: BaseRepository() {

    private var autenticacao: FirebaseAuth? = ConfiguracaoFirebase.firebaseAutenticacao

    @ExperimentalCoroutinesApi
    suspend fun signUp(nome: String, email: String, password: String): Boolean {
        return try {
            val data = autenticacao
                ?.createUserWithEmailAndPassword(email, password)?.await()
            val user = autenticacao?.currentUser?.uid?.let { User(it, nome, email) }
            user?.let {
                registerUserIntoFirebase(user)
            } ?: run { false }

            return data != null
        } catch (e: java.lang.Exception) {
            false
        }
    }


    private suspend fun registerUserIntoFirebase(userInfo: User?): Boolean? {

        return try {
            ConfiguracaoFirebase.autenticacao?.currentUser?.uid?.let { Uid ->
                val documentSnapshot = userInfo?.let { userInfo ->
                    ConfiguracaoFirebase.fireStore?.collection(Constants.USERS)
                        ?.document(Uid)
                        ?.set(userInfo, SetOptions.merge())
                        ?.await()
                }
                documentSnapshot != null
            }
        } catch (e: java.lang.Exception) {
            e.cause
            return false
        }
    }

    suspend fun logInWithEmail(
        email: String, password: String
    ): User? {
        return try {
            val data = autenticacao?.signInWithEmailAndPassword(email, password)?.await()
            data?.let {
                return signInUserIntoFirebase()
            }
        } catch (e: java.lang.Exception) {
            null
        }

    }

    suspend fun getUser(): User? {
        return signInUserIntoFirebase()
    }

    private suspend fun signInUserIntoFirebase(): User? { //Todo changing SignInActivity to Activity, in order to embrace not only one activity.
        return try {
            ConfiguracaoFirebase.autenticacao?.currentUser?.uid?.let { Uid ->
                val documentSnapshot = ConfiguracaoFirebase.fireStore
                    ?.collection(Constants.USERS)
                    ?.document(Uid)
                    ?.get()?.await()
                return documentSnapshot?.toObject(User::class.java)
            }
        } catch (e: java.lang.Exception) {
            Log.e("SignInException","${e.cause}")
            null
        }
    }

    fun getImage(selectedUri: Uri, onComplete: (Uri?, String) -> Unit ) {
        return uploadUserImageFirebase(selectedUri, onComplete)
    }

    private fun uploadUserImageFirebase(selectedUri: Uri, onComplete: (Uri?, String) -> Unit ) {
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child("$IMAGE/${autenticacao?.currentUser?.email}/${System.currentTimeMillis()}_${selectedUri.lastPathSegment
                ?.split(SLACK)?.last() ?: DEFAULT_UPLOAD}"
            )
        ref.putFile(selectedUri).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {uri ->
               onComplete(uri, "")

            }
        }.addOnFailureListener{exception->
            onComplete(null, exception.message ?: "Erro desconhecido")
        }
    }

    fun getChanges(hashMap: HashMap<String, Any>, onComplete:(Uri?, String)-> Unit){
        anyChangeVerified(hashMap, onComplete)
    }

    private fun anyChangeVerified(userHashMap:HashMap<String, Any>,
                                          onComplete:(Uri?, String) -> Unit
                                          ) {
        val ref: StorageReference = FirebaseStorage.getInstance().reference
        try {
            autenticacao?.currentUser?.uid?.let { uid ->
                ConfiguracaoFirebase.fireStore
                    ?.collection(Constants.USERS)
                    ?.document(uid)
                    ?.set(userHashMap, SetOptions.merge())
                    ?.addOnSuccessListener {
                        onComplete(Uri.parse(userHashMap[IMAGE] as String?), "")
                    }?.addOnFailureListener{
                        onComplete(null, it.localizedMessage ?: "Erro ao atualizar")
                    }
            }
        } catch (e: java.lang.Exception) {
            onComplete(null, "${e.message}")
        }
    }


}