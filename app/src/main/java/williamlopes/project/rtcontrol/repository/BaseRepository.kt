package williamlopes.project.rtcontrol.repository

import com.google.android.gms.common.api.Response
import williamlopes.project.rtcontrol.network.ApiResult

open class BaseRepository {
/*
    suspend fun <T> safeCallApi (call: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = call.invoke()

            if (response.isSuccessful) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.message()?: "Não foi possível fazer a chamada.")
            }
        } catch (error: Exception) {
            ApiResult.Error("Não foi possível fazer a chamada.")
        }
    }*/
}