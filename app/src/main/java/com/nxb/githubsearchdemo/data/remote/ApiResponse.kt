package com.nxb.githubsearchdemo.data.remote

import android.util.Log
import retrofit2.Response

/**
@author Salman Aziz
created on 6/24/21
 **/

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }
        fun <T> create(response: Response<T>): ApiResponse<T> {
            Log.d( "networkdebug", "success: " + response.body().toString() )
            return if(response.isSuccessful) {
                Log.d( "networkdebug", "success: " + response.body().toString() )
                val body = response.body()
                // Empty body
                if (body == null || response.code() == 204) {
                    ApiSuccessEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                Log.d( "networkdebug", "error: " + msg.toString() )
                val errorMessage = if(msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMessage ?: "Unknown error")
            }
        }
    }
}

class ApiSuccessResponse<T>(val body: T): ApiResponse<T>()
class ApiSuccessEmptyResponse<T>: ApiResponse<T>()
class ApiErrorResponse<T>(val errorMessage: String): ApiResponse<T>()