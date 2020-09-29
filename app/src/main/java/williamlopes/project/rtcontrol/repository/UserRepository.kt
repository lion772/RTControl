package williamlopes.project.rtcontrol.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase.registerUserIntoFirebase
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase.signInUserIntoFirebase
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase.signInUserIntoFirebaseTeste
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.util.await
import williamlopes.project.rtcontrol.view.SignInActivity
import williamlopes.project.rtcontrol.view.SignUpActivity

class UserRepository(
    private val signupActivity: SignUpActivity,
    private val signinActivity: SignInActivity

) : BaseRepository() {

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
            if (user != null) {
                registerUserIntoFirebase(signupActivity, user)
            }
            return data != null
        } catch (e: java.lang.Exception) {
            false
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun logInWithEmail(
        email: String, password: String
    ): AuthResult? {
        return try {
            val data = autenticacao?.signInWithEmailAndPassword(email, password)?.await()
            data?.let {
                signInUserIntoFirebaseTeste(signinActivity)
                return data
            }
        } catch (e: java.lang.Exception) {
            null
        }

    }

    fun getFirebaseUser() {
        autenticacao?.signInAnonymously()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                actualUser = task.result?.user
                _firebaseUser.postValue(actualUser)
            } else {
                // If sign in fails, display a message to the user.
                actualUser = null
                _firebaseUser.postValue(actualUser)
            }
        }
    }


}