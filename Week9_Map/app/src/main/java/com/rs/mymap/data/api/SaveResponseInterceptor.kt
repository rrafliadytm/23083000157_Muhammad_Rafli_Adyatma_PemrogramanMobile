package com.rs.mymap.data.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.File

class SaveResponseInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful) {
            val responseBody = response.peekBody(Long.MAX_VALUE)
            val json = responseBody.string()
            saveToFile(json)
        }
        return response
    }

    private fun saveToFile(json: String) {
        try {
            val file = File(context.filesDir, "direction_response.json")
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
