package williamlopes.project.rtcontrol.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.usecase.UserUseCase

class MyProfileViewModel(
    application: Application,
    private val userUseCase: UserUseCase
): BaseViewModel(application) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    @ExperimentalCoroutinesApi
    fun getUser(){
        launch {
            userUseCase.getNomeUserInfo()?.let {userInfo ->
                _user.postValue(userInfo)
            }
        }
    }

}