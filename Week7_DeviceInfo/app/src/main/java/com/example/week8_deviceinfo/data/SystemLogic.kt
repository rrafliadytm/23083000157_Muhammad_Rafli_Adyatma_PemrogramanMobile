package com.example.week8_deviceinfo.data

import android.app.ActivityManager
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.File
import kotlin.math.sqrt

object SystemLogic {

    /**
     * Mendapatkan detail RAM (Total & Tersedia) dalam GB.
     */
    fun getRamDetails(context: Context): Pair<String, String> {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)

            val totalRam = memoryInfo.totalMem / (1024.0 * 1024.0 * 1024.0)
            val availableRam = memoryInfo.availMem / (1024.0 * 1024.0 * 1024.0)

            Pair("%.2f GB".format(totalRam), "%.2f GB".format(availableRam))
        } catch (e: Throwable) {
            Pair("N/A", "N/A")
        }
    }

    /**
     * Mendapatkan detail Penyimpanan Internal (Total & Tersedia) dalam GB.
     */
    fun getStorageDetails(): Pair<String, String> {
        return try {
            val path: File = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            val availableBlocks = stat.availableBlocksLong

            val totalStorage = (totalBlocks * blockSize) / (1024.0 * 1024.0 * 1024.0)
            val availableStorage = (availableBlocks * blockSize) / (1024.0 * 1024.0 * 1024.0)

            Pair("%.2f GB".format(totalStorage), "%.2f GB".format(availableStorage))
        } catch (e: Throwable) {
            Pair("N/A", "N/A")
        }
    }

    /**
     * Menghitung ukuran layar dalam Inci menggunakan rumus Pythagoras.
     */
    fun getScreenSizeInInches(context: Context): String {
        return try {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.display?.getRealMetrics(dm)
            } else {
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getRealMetrics(dm)
            }

            val x = (dm.widthPixels / dm.xdpi).toDouble()
            val y = (dm.heightPixels / dm.ydpi).toDouble()
            val screenInches = sqrt(x * x + y * y)

            "%.1f Inci".format(screenInches)
        } catch (e: Throwable) {
            "N/A"
        }
    }

    /**
     * Mendeteksi resolusi kamera belakang utama dalam Megapixel (MP).
     */
    fun getCameraMegapixels(context: Context): String {
        return try {
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIdList = manager.cameraIdList
            var maxMegapixels = 0.0

            for (id in cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(id)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                
                if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                    val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    val sizes = map?.getOutputSizes(ImageFormat.JPEG)
                    
                    if (sizes != null && sizes.isNotEmpty()) {
                        val largest = sizes[0]
                        val megapixels = (largest.width * largest.height).toDouble() / 1000000.0
                        if (megapixels > maxMegapixels) {
                            maxMegapixels = megapixels
                        }
                    }
                }
            }
            if (maxMegapixels > 0) "%.1f MP".format(maxMegapixels) else "N/A"
        } catch (e: Throwable) {
            "N/A"
        }
    }
}
