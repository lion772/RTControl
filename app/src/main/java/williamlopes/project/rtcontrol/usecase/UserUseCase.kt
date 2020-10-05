package williamlopes.project.rtcontrol.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.repository.UserRepository

class UserUseCase(private val userRepository: UserRepository) {

    @ExperimentalCoroutinesApi
    suspend fun signUpAwait(nome:String, email:String, password:String) =
        userRepository.signUp(nome, email, password)

    @ExperimentalCoroutinesApi
    suspend fun signInAwait(email:String, password:String): User? =
        userRepository.logInWithEmail(email, password)

    suspend fun getNomeUserInfo():User? = userRepository.getUser()
}