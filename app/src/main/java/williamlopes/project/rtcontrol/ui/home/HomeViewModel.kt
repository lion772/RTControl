package williamlopes.project.rtcontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.usecase.UserUseCase

class HomeViewModel(
    private val userUseCase: UserUseCase
): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun getUser(){
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.getNomeUserInfo()?.let {userInfo ->
                _user.postValue(userInfo)
            }
        }
    }
}