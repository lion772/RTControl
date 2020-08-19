package williamlopes.project.rtcontrol.service.repository

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import williamlopes.project.rtcontrol.service.model.User
import williamlopes.project.rtcontrol.service.model.UserModel

interface DataService {

    @GET("users/{id}")
    fun getUser(@Path ("id") id:Int): Call<UserModel?>?

    @POST("users/{id}")
    fun insertUser(@Path ("id") id:Int, @Body user: User): Call<UserModel?>?
}