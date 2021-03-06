package williamlopes.project.rtcontrol.usecase

import android.net.Uri
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.repository.UserRepository

@ExperimentalCoroutinesApi
class UserUseCase(private val userRepository: UserRepository) {

    suspend fun signUpAwait(nome:String, email:String, password:String) =
        userRepository.signUp(nome, email, password)

    suspend fun signInAwait(email:String, password:String): User? =
        userRepository.logInWithEmail(email, password)

    suspend fun getNomeUserInfo():User? = userRepository.getUser()

    fun updateProfileImage(selectedUri:Uri, onComplete: (Uri?, String) -> Unit) = userRepository.getImage(selectedUri, onComplete)

    fun anyChangeVerifiedCase(hashMap: HashMap<String, Any>,
                                      onComplete:(Uri?, String) -> Unit)
            = userRepository.getChanges(hashMap, onComplete)


}