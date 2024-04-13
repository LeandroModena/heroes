package dev.modena.heroes.shared.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import dev.modena.heroes.data.local.entity.Hero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ShareImage {

    private fun shareImage(
        context: Context,
        imageUri: Uri,
        shareMessage: String
    ) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/png"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(Intent.createChooser(shareIntent, shareMessage))
    }

    private suspend fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap?
    ): Uri? = withContext(Dispatchers.IO) {
        val filename = "share_image_${System.currentTimeMillis()}.png"
        val outputStream: OutputStream
        val imageUri: Uri

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val resolver = context.contentResolver
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: return@withContext null
            outputStream = resolver.openOutputStream(imageUri) ?: return@withContext null
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            imageUri = Uri.fromFile(image)
            outputStream = FileOutputStream(image)
        }

        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return@withContext imageUri
    }


    suspend fun async(
        context: Context,
        hero: Hero,
        shareMessage: String,
        onError: () -> Unit
    ) {

        val imageListener = object : ImageRequest.Listener {

            override fun onError(request: ImageRequest, result: ErrorResult) {
                super.onError(request, result)
                onError.invoke()
            }

            override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                super.onSuccess(request, result)
                CoroutineScope(Dispatchers.IO).launch {
                    saveBitmapToFile(context, result.drawable.toBitmap())?.let { uri ->
                        withContext(Dispatchers.Main) {
                            shareImage(context, uri, shareMessage)
                        }
                    } ?: onError.invoke()
                }
            }
        }

        val imageRequest = ImageRequest.Builder(context)
            .data(hero.thumbnailURL)
            .listener(imageListener)
            .build()

        val imageLoader = Coil.imageLoader(context)
        imageLoader.execute(imageRequest)
    }

}