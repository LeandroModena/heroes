package dev.modena.heroes.home.detail

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult

class SupportImage(context: Context) {


    val imageLoader = Coil.imageLoader(context)

    val imageListener = object : ImageRequest.Listener {
        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
            result.drawable.toBitmap()
        }

    }


}