package com.krobys.skytest.retrofit.interceptors

import com.krobys.skytest.retrofit.data.network.RequestParams.CONTENT_TYPE
import com.krobys.skytest.retrofit.data.network.RequestParams.CONTENT_TYPE_VALUE
import com.krobys.skytest.retrofit.data.network.RequestParams.CONTROLS_ACCEPT
import com.krobys.skytest.retrofit.data.network.RequestParams.CONTROLS_ACCEPT_VALUE
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        return@runBlocking try {
            val request: Request = chain.request()

            val newRequest: Request = request.newBuilder().apply {
                addHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                addHeader(CONTROLS_ACCEPT, CONTROLS_ACCEPT_VALUE)
            }.build()
            val response: Response = chain.proceed(newRequest)
            response
        } catch (e: Exception) {
            throw e
        }
    }
}