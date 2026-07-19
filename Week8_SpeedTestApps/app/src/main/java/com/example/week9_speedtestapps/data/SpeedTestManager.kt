package com.example.week9_speedtestapps.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

class SpeedTestManager {

    suspend fun testPing(): Float = kotlinx.coroutines.withContext(Dispatchers.IO) {
        try {
            // Gunakan timeout pada process untuk menghindari hang
            val process = Runtime.getRuntime().exec("ping -c 3 -W 2 google.com")
            val reader = process.inputStream.bufferedReader()
            var totalTime = 0f
            var count = 0
            val pattern = Pattern.compile("time=(\\d+\\.?\\d*)\\s*ms")
            
            // Baca baris demi baris dengan aman
            reader.useLines { lines ->
                lines.forEach { line ->
                    val matcher = pattern.matcher(line)
                    if (matcher.find()) {
                        totalTime += matcher.group(1)?.toFloat() ?: 0f
                        count++
                    }
                }
            }

            if (count > 0) totalTime / count else fallbackPing()
        } catch (e: Exception) {
            fallbackPing()
        }
    }

    private suspend fun fallbackPing(): Float = kotlinx.coroutines.withContext(Dispatchers.IO) {
        try {
            val start = System.nanoTime()
            val url = URL("https://www.google.com")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.connect()
            connection.disconnect()
            (System.nanoTime() - start) / 1_000_000f
        } catch (e: Exception) {
            0f
        }
    }

    fun testDownload(urlStr: String = "https://speed.cloudflare.com/__down?bytes=10000000"): Flow<Pair<Float, Float>> = flow {
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val fileSize = 10_000_000 // 10MB
            var bytesRead = 0L
            val startTime = System.currentTimeMillis()
            var lastUpdate = System.currentTimeMillis()
            
            connection.inputStream.use { input ->
                val buffer = ByteArray(8192)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    bytesRead += read
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastUpdate >= 150) {
                        val elapsedSec = (currentTime - startTime) / 1000f
                        val mbps = if (elapsedSec > 0) (bytesRead * 8f) / (elapsedSec * 1_000_000f) else 0f
                        val progress = bytesRead.toFloat() / fileSize
                        emit(progress to mbps)
                        lastUpdate = currentTime
                    }
                }
            }
            val totalTime = (System.currentTimeMillis() - startTime) / 1000f
            emit(1f to (bytesRead * 8f) / (totalTime * 1_000_000f))
        } finally {
            connection.disconnect()
        }
    }.flowOn(Dispatchers.IO)

    fun testUpload(urlStr: String = "https://speed.cloudflare.com/__up"): Flow<Pair<Float, Float>> = flow {
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val uploadSize = 5_000_000 // 5MB
            connection.doOutput = true
            connection.requestMethod = "POST"
            connection.setFixedLengthStreamingMode(uploadSize)
            
            val startTime = System.currentTimeMillis()
            var lastUpdate = System.currentTimeMillis()
            var bytesWritten = 0L
            
            connection.outputStream.use { output ->
                val buffer = ByteArray(8192)
                while (bytesWritten < uploadSize) {
                    val toWrite = minOf(buffer.size, (uploadSize - bytesWritten).toInt())
                    output.write(buffer, 0, toWrite)
                    bytesWritten += toWrite
                    
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastUpdate >= 150) {
                        val elapsedSec = (currentTime - startTime) / 1000f
                        val mbps = if (elapsedSec > 0) (bytesWritten * 8f) / (elapsedSec * 1_000_000f) else 0f
                        val progress = bytesWritten.toFloat() / uploadSize
                        emit(progress to mbps)
                        lastUpdate = currentTime
                    }
                }
            }
            // Read response to complete the request
            connection.inputStream.use { it.readBytes() }
            
            val totalTime = (System.currentTimeMillis() - startTime) / 1000f
            emit(1f to (bytesWritten * 8f) / (totalTime * 1_000_000f))
        } finally {
            connection.disconnect()
        }
    }.flowOn(Dispatchers.IO)
}
