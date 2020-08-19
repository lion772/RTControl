package williamlopes.project.rtcontrol.service.repository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import williamlopes.project.rtcontrol.service.model.User
import williamlopes.project.rtcontrol.service.model.UserModel

class UserRepository(context: Context) {

    private val retrofit: DataService = RetrofitClient.createService(DataService::class.java)
    private val database = UserDataBase.getDatabase(context = context).userDao()
    private var getUser: Call<UserModel?>? = null
    private var insertUser: Call<UserModel?>? = null

    fun insertUser(user: User?){
        insertUser = user?.let { retrofit.insertUser(id = it.id, user = it) }
        insertUser?.enqueue(object : Callback<UserModel?> {
            override fun onResponse(call: Call<UserModel?>, response: Response<UserModel?>) {

            }
            override fun onFailure(call: Call<UserModel?>, t: Throwable) {

            }
        })
    }

    fun getUser(user: UserModel?){
        getUser = user?.id?.let { retrofit?.getUser(id = it) }
        getUser?.enqueue(object : Callback<UserModel?> {
            override fun onResponse(call: Call<UserModel?>, response: Response<UserModel?>) {

            }
            override fun onFailure(call: Call<UserModel?>, t: Throwable) {

            }
        })
    }
}