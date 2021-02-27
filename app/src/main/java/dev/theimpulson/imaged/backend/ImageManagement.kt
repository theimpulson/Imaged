package dev.theimpulson.imaged.backend

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.ImageResult
import java.io.ByteArrayOutputStream

class ImageManagement {

    companion object {
        suspend fun loadAndSaveImage(
            imageView: ImageView,
            progressBar: ProgressBar,
            context: Context
        ) {
            /**
             * Loads a random image from Lorem Picsum website and
             * saves it as a base64 string using SharedPreferences
             */
            val BASE_URL = "https://picsum.photos/500"

            val imageLoader = imageView.context.imageLoader
            val request = ImageRequest.Builder(imageView.context)
                .data(BASE_URL)
                .target(imageView)
                .listener(onStart = {
                    progressBar.visibility = View.VISIBLE
                }, onSuccess = { request: ImageRequest, metadata: ImageResult.Metadata ->
                    progressBar.visibility = View.INVISIBLE
                })
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()
            imageLoader.execute(request)

            // Save the image from the ImageView
            saveImage(imageView, context)
        }

        fun saveImage(imageView: ImageView, context: Context) {
            /**
             * Saves the current image from the imageView widget
             * in MainActivity
             */
            val sharedPref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

            val baos = ByteArrayOutputStream()
            val bitmap = imageView.drawable.toBitmap()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

            with(sharedPref.edit()) {
                putString("encodedImage", encodedImage)
                apply()
            }
        }

        fun loadLastImage(imageView: ImageView, context: Context) {
            /**
             * Loads the last saved image from SharedPreferences into the
             * MainActivity's ImageView
             */
            val sharedPref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            val encodedImage = sharedPref.getString("encodedImage", "DEFAULT")

            if (encodedImage != "DEFAULT") {
                val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageView.load(decodedImage)
            }
        }
    }
}