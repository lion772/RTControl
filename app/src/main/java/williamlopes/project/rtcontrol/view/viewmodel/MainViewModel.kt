package williamlopes.project.rtcontrol.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import williamlopes.project.rtcontrol.service.model.User
import williamlopes.project.rtcontrol.service.repository.UserDataBase
import williamlopes.project.rtcontrol.service.repository.UserRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repository = UserRepository(context)
    private val database = UserDataBase.getDatabase(context = context)

    private val mUser = MutableLiveData<User>()
    val user: LiveData<User>
        get() = mUser

    private val _isUpdating = MutableLiveData<Boolean>()
    val isUpdating: LiveData<Boolean>
        get() = _isUpdating

    fun login() {
        val usuario = mUser.value
        if (usuario?.id != DEFAULT_VALUE){
            if (!usuario?.email.isNullOrEmpty() && !usuario?.password.isNullOrEmpty()){
                repository?.apply {
                    insertUser(usuario)
                }
            }
        }else {
            if (!usuario?.email.isNullOrEmpty() && !usuario?.password.isNullOrEmpty()){
                repository?.apply {
                    getUser(user = usuario)
                }
            }
        }
    }

    companion object{
        private const val DEFAULT_VALUE = -1
    }
}