package williamlopes.project.rtcontrol.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
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

    private val _profileImage = MutableLiveData<Uri>()
    val profileImage: LiveData<Uri> get() = _profileImage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    @ExperimentalCoroutinesApi
    fun getUser(){
        launch {
            userUseCase.getNomeUserInfo()?.let {userInfo ->
                _user.postValue(userInfo)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun updateProfileImage(uri: Uri?){
        _isLoading.value = true
        uri?.let {
            userUseCase.updateProfileImage(it) { uriImage, error ->
                if (error.isNotEmpty()){
                    val test = error
                }else{
                    _profileImage.postValue(uriImage)
                }
                _isLoading.postValue(false)
            }
        }
    }

}