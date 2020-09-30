package williamlopes.project.rtcontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.usecase.UserUseCase

class LoginViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isUserCreated = MutableLiveData<Boolean>()
    val isUserCreated: LiveData<Boolean> get() = _isUserCreated

    private var _firebaseUser = MutableLiveData<AuthResult>()
    val firebaseUser: LiveData<AuthResult> get() = _firebaseUser


    @ExperimentalCoroutinesApi
    fun signUp(nome:String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = userUseCase.signUpAwait(nome, email, password)
            if (response){
                _isUserCreated.postValue(true)
                _isLoading.postValue(false)
            }else{
                _isUserCreated.postValue(false)
                _isLoading.postValue(false)
            }

        }
    }

    @ExperimentalCoroutinesApi
    fun signIn(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = userUseCase.signInAwait(email, password)
                if (response != null){
                    _isUserCreated.postValue(true)
                    _isLoading.postValue(false)
                } else {
                    _isUserCreated.postValue(false)
                    _isLoading.postValue(false)
                }
        }
    }

}