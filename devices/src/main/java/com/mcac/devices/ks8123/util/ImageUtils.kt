package com.mcac.devices.ks8123.util

import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
import android.util.Base64
import java.io.*
import java.nio.ByteBuffer


object ImageUtils {


    fun saveYuv2Jpeg(path: String, data: ByteArray): Int {
        val yuvImage = YuvImage(data, ImageFormat.NV21, 640, 480, null)
        val bos = ByteArrayOutputStream(data.size)
        val result = yuvImage.compressToJpeg(Rect(0, 0, 640, 480), 100, bos)
        if (result) {
            val buffer = bos.toByteArray()
            try {
                val newfile = File(path)
                if (!newfile.parentFile.exists()) newfile.parentFile.mkdirs()
                val file = File(path)
                val fos = FileOutputStream(file)
                fos.write(buffer)
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return -1
            } catch (e: IOException) {
                e.printStackTrace()
                return -1
            }
        }
        try {
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 0
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun base64ToBitmap(data: String): Bitmap{
        val imageBytes = Base64.decode(data, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
    fun bitmapToBase64(bitmap: Bitmap):String {
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        val imageBytes = byteArray.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
    fun bitmapToBase64WithoutCompress(bitmap: Bitmap):String {
        val buffer: ByteBuffer = ByteBuffer.allocate(bitmap.rowBytes * bitmap.height)
        bitmap.copyPixelsToBuffer(buffer)
        val data: ByteArray = buffer.array()
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    fun textToBitmap(
        text: String,
        textSize: Float = 23f,
        align: Paint.Align = Paint.Align.RIGHT
    ): Bitmap {
        val paint = TextPaint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.textSize = textSize
        paint.textAlign = align
        paint.isAntiAlias = true
        var mTextLayout = StaticLayout.Builder.obtain(text, 0, text.length, paint, 380)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setTextDirection(TextDirectionHeuristics.RTL)
            .setLineSpacing(0f, 1f)
            .build()

        val bitmap = Bitmap.createBitmap(380, mTextLayout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        mTextLayout.draw(canvas)

        return bitmap
    }
    fun resizeImage(bitmap: Bitmap, w: Int, h: Int): Bitmap? {
        val bitmapOrg = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val width = bitmapOrg.width
        val height = bitmapOrg.height
        val scaleWidth = w.toFloat() / width.toFloat()
        val scaleHeight = h.toFloat() / height.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true)
    }
}