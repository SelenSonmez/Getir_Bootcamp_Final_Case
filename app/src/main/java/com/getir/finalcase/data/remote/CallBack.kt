package com.getir.finalcase.data.remote

import com.getir.finalcase.domain.model.BaseResponse

import kotlinx.coroutines.channels.SendChannel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Callback class for handling responses from Retrofit API calls.
 * Sends the response or error through a channel.
 *
 * @param responseChannel The channel to send the response or error.
 */
class CallBack<T>(private val responseChannel: SendChannel<BaseResponse<T>>): Callback<T> {

    /**
     * Invoked for a received HTTP response.
     * Sends the successful response through the channel.
     * If the response body is null, sends an error response with a message "Body is null".
     *
     * @param call The call object used to make the request.
     * @param response The response received from the server.
     */
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if(response.isSuccessful){
            val body = response.body()
            if(body != null) {
                responseChannel.trySend(BaseResponse.Success(body))
            }else {
                responseChannel.trySend(BaseResponse.Error(message = "Body is null"))
            }
        } else {
            val errorBody = response.errorBody().toString()
            responseChannel.trySend(BaseResponse.Error(errorBody))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        responseChannel.trySend(BaseResponse.Error(message = t.localizedMessage))
    }


}