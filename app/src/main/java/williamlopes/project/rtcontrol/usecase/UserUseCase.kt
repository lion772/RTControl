package williamlopes.project.rtcontrol.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.repository.UserRepository

@ExperimentalCoroutinesApi
class UserUseCase(private val userRepository: UserRepository) {

    suspend fun signUpAwait(nome:String, email:String, password:String) =
        userRepository.signUp(nome, email, password)

    suspend fun signInAwait(email:String, password:String): User? =
        userRepository.logInWithEmail(email, password)

    /*
    suspend fun setUserInfo() = userRepository.registerUserIntoFirebase()*/

    suspend fun getNomeUserInfo():User? = userRepository.getUser()


}