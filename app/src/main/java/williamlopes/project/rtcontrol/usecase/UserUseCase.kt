package williamlopes.project.rtcontrol.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.repository.UserRepository

class UserUseCase(private val userRepository: UserRepository) {

    @ExperimentalCoroutinesApi
    suspend fun signUpAwait(nome:String, email:String, password:String) =
        userRepository.signUp(nome, email, password)

    @ExperimentalCoroutinesApi
    suspend fun signInAwait(email:String, password:String) =
        userRepository.logInWithEmail(email, password)

    fun alreadyLoggedIn(userFirebase: FirebaseAuth) = userRepository.getFirebaseUser()

}