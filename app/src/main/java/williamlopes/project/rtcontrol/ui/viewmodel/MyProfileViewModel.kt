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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _profileImageUpload = MutableLiveData<Uri>()
    val profileImageUpload: LiveData<Uri> get() = _profileImageUpload

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
            userUseCase.updateProfileImage(it) { uriImage, errorMessage ->
                if (errorMessage.isNotEmpty()){
                    _error.postValue(errorMessage)
                }else{
                    _profileImage.postValue(uriImage)
                }
                _isLoading.postValue(false)
            }
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun anyChangeVerified(hashMap: HashMap<String, Any>?){
        _isLoading.value = true
        hashMap?.let {
            userUseCase.anyChangeVerifiedCase(it){ mapChange, errorMessage ->
                if (errorMessage.isNotEmpty()){
                    _error.postValue(errorMessage)
                } else {
                    _profileImageUpload.postValue(mapChange)
                }
                _isLoading.postValue(false)
            }
        }
    }

}