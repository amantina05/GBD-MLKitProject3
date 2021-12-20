package com.example.p3mlkit

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //when this is clicked we want to be able to take a picture
        val button = findViewById<Button>(R.id.button).setOnClickListener {
            dispatchTakePictureIntent()
        }
        val imageView = findViewById<ImageView>(R.id.imageView)
    }
//takes a photo with the camera app
    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        //allows an image to be captured on the camera
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //try to capture if not
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            //display error
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

//gets the thumbnail, get the data and transform it into a bitmap then setting the bitmap to the homescreen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            //rotates image
            val matrix = Matrix()
            matrix.postRotate(90F)
            val rotatedBitmap = Bitmap.createBitmap(
                imageBitmap,
                0,
                0,
                imageBitmap.width,
                imageBitmap.height,
                matrix,
                false
            )
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(rotatedBitmap)

            val image = InputImage.fromBitmap(rotatedBitmap, 0)

            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(image)
                .addOnSuccessListener { labels ->
//                    findViewById<TextView>(R.id.textView).text = labels.subList(fromIndex=0, toIndex=5).toString()
//                    findViewById<TextView>(R.id.textView).text = labels.subList(0, 5).toString()
//                    findViewById<TextView>(R.id.textView).text = labels.toString()
                    findViewById<TextView>(R.id.textView).text = labels.toString()

                }
                .addOnFailureListener { e ->
                    findViewById<TextView>(R.id.textView).text = e.toString()

                }
        }
    }

}