package williamlopes.project.rtcontrol.service.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object{

        private lateinit var retrofit: Retrofit
        private val baseUrl = ""

        private fun getRetrofitInstance(): Retrofit{
            val httpClient = OkHttpClient.Builder().build()
            if (!::retrofit.isInitialized){
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

        fun <T> createService(serviceClass: Class<T>): T = getRetrofitInstance().create(serviceClass)

    }
}