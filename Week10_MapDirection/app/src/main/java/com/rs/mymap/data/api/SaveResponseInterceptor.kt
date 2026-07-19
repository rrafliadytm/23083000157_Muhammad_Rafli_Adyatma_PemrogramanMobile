package com.rs.mymap.data.api

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.io.File

class SaveResponseInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful) {
            val responseBody = response.peekBody(Long.MAX_VALUE)
            val jsonString = responseBody.string()
            
            CoroutineScope(Dispatchers.IO).launch {
                saveToFile(jsonString)
            }
        }
        return response
    }

    private fun saveToFile(jsonString: String) {
        try {
            val file = File(context.filesDir, "direction_response.json")
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
