package com.krobys.skytest.retrofit.resultCallAdapter

/*
*  File Name: ResultCall.kt
*  Description: This file is part of a system for returning the result from retrofit as Result<T>
*  Author: Andrii Kryvonos
*/

import com.krobys.skytest.BuildConfig
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CustomError(
    val code: Int,
    override val message: String
) : Throwable()

class ResultCall<T>(private val delegate: Call<T>, private val handleUnauthorised: (()->Unit)) :
    Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) = delegate.enqueue(
        object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@ResultCall,
                    Response.success(response.toResult())
                )
            }

            private fun Response<T>.toResult(): Result<T> {
                if (!isSuccessful) {
                    when (code()) {
                        401 -> {
                            handleUnauthorised()
                            Result.failure<T>(
                                CustomError(
                                    this.code(),
                                    "Unauthorised"
                                )
                            )
                        }
                        else -> {
                            Result.failure<T>(
                                CustomError(
                                    this.code(),
                                    "Error occurred while processing server response"
                                )
                            )
                        }
                    }
                }

                body()?.let { body ->
                    return Result.success(body)
                }

                return if (body() == null) {
                    @Suppress("UNCHECKED_CAST")
                    Result.success(Unit) as Result<T>
                } else {
                    Result.failure(CustomError(-1, "Failed to retrieve server response"))
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val response: Result<T> = if (!BuildConfig.DEBUG) {
                    when (throwable) {
                        is SocketTimeoutException -> {
                            Result.failure(CustomError(-1, "Connection to server timed out"))
                        }

                        is SocketException -> {
                            Result.failure(CustomError(-1, "A network connection error occurred"))
                        }

                        is UnknownHostException -> {
                            Result.failure(CustomError(-1, "Failed to connect to server"))
                        }

                        else -> {
                            Result.failure(
                                CustomError(
                                    -1,
                                    "An unknown error occurred: ${throwable.message ?: ""}"
                                )
                            )
                        }
                    }
                } else {
                    Result.failure(throwable)
                }
                callback.onResponse(this@ResultCall, Response.success(response))
            }
        }
    )

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(delegate.execute().body()!!))
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun clone(): Call<Result<T>> {
        return ResultCall(delegate.clone(), handleUnauthorised = handleUnauthorised)
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }
}
