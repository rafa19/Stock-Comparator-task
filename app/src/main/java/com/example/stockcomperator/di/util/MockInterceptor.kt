package com.example.stockcomperator.di.util

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import javax.inject.Singleton


@Singleton
class MockInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            /*
            if url contains 'localfile' then getting json response from assets
            else continuing network call
             */
            if (chain.request().url.host.contains("localfile")) {
                val type = chain.request().url.queryParameter("type")
                val jsonString = context.assets.open(type.orEmpty()).bufferedReader().use {
                    it.readText()
                }
                Response.Builder()
                    .code(200)
                    .message(jsonString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(jsonString.toResponseBody("application/json".toMediaTypeOrNull()))
                    .addHeader("content-type", "application/json")
                    .build()
            } else {
                chain.proceed(chain.request())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            chain.proceed(chain.request())
        }
    }

}