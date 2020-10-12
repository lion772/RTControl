package williamlopes.project.rtcontrol.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.util.Constants
import williamlopes.project.rtcontrol.util.await

@ExperimentalCoroutinesApi
class UserRepository : BaseRepository() {

    private var autenticacao: FirebaseAuth? = ConfiguracaoFirebase.firebaseAutenticacao
    private var actualUser: FirebaseUser? = null
    private val _auth = FirebaseAuth.getInstance()

    private var _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private var _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> get() = _firebaseUser

    private var _userFound = MutableLiveData<Boolean>()
    val userFound: LiveData<Boolean> get() = _userFound


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

}