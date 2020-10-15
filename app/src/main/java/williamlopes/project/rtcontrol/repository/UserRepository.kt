package williamlopes.project.rtcontrol.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.common.io.Files.getFileExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.util.Constants
import williamlopes.project.rtcontrol.util.await
import java.io.File


@ExperimentalCoroutinesApi
class UserRepository : BaseRepository() {

    private var autenticacao: FirebaseAuth? = ConfiguracaoFirebase.firebaseAutenticacao
    private var profileImageURL: Uri? = null
    private val contentResolver: ContentResolver? = null

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
            e.cause
            null
        }
    }

    suspend fun getImage(selectedUri: Uri): Uri? {
        return uploadUserImageFirebase(selectedUri)
    }


    private suspend fun uploadUserImageFirebase(selectedUri: Uri): Uri? {
        val ref: StorageReference? = FirebaseStorage.getInstance().reference
        return try {
            val file = Uri.fromFile(File("${selectedUri.path}"))
            val riversRef:StorageReference? = ref
                ?.child("images/${file.lastPathSegment}")
            riversRef?.putFile(file)?.await()
            return riversRef?.downloadUrl?.await()
        } catch (e: Exception) {
            Log.e("uploadUserImage: ", e.message.toString())
            null
        }
    }

    /*selectedImageFileUri?.let { UriFileSelected ->
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child("USER_IMAGE + ${System.currentTimeMillis()}" +
                    " + . + ${getFileExtension(UriFileSelected)}"
            )
        ref.putFile(UriFileSelected).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {uri ->
                profileImageURL = uri.toString()
                Log.e("dowloadableUserImage", uri.toString())
                //TODO Update User Profile Data.
                hideProgressDialog()
            }
        }
    }?.addOnFailureListener{exception->
        Toast.makeText(context , exception.message, Toast.LENGTH_SHORT).show()
        hideProgressDialog()
    }*/

}