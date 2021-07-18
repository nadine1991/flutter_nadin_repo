package com.example.gallery_android

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class Add_Image : AppCompatActivity() {

    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image)

        val imageperson: ImageView = findViewById(R.id.imageperson)
        imageperson.setOnClickListener( View.OnClickListener {
           checkPermission()

            Toast.makeText(applicationContext,"Clicked",Toast.LENGTH_LONG).show()

        })
        Toast.makeText(applicationContext,myRef.toString(),Toast.LENGTH_LONG).show()





        }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }


    val READIMAGE:Int=253
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf( android.Manifest.permission.CAMERA),READIMAGE)
                return
            }
        }

        loadImage()
    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            READIMAGE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images",Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }


    }

    val REQUEST_IMAGE_CAPTURE = 1
    fun loadImage(){

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageperson: ImageView = findViewById(R.id.imageperson)
            imageperson.setImageBitmap(imageBitmap)
        }

    }



    fun AddFeed(view:View){
        val title: TextView = findViewById(R.id.title)
        val detail: TextView = findViewById(R.id.detail)

        var titletext = title.text.toString()
        var detailtext = detail.text.toString()

        SaveToFireBase(titletext,detailtext)
    }



   // val imageperson: ImageView = findViewById(R.id.imageperson)
    fun SaveToFireBase( title:String, des:String)
    {

        var DownloadURL = ""
        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://car-store-298a7.appspot.com/images")
        val df= SimpleDateFormat("ddMMyyHHmmss")
        val dataobj= Date()
        val imagePath= title + "."+ df.format(dataobj)+ ".jpg"
        val ImageRef=storgaRef.child(imagePath)

        val imageperson: ImageView = findViewById(R.id.imageperson)
        imageperson.isDrawingCacheEnabled=true
        imageperson.buildDrawingCache()
        val drawable=imageperson.drawable as BitmapDrawable
        val bitmap=drawable.bitmap

        val baos= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data= baos.toByteArray()
        val uploadTask=ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"fail to upload",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->





            Toast.makeText(applicationContext,"ImageRef"+ImageRef,Toast.LENGTH_LONG).show()


            println("imagerefff "+ImageRef)
                    ImageRef.getDownloadUrl().addOnSuccessListener(OnSuccessListener<Any> { uri ->
                    DownloadURL = uri.toString()

                    println("DownloadURL imagerefff "+DownloadURL)



                        var id  =  getRandomString(9)
                        println("title"+title)
                        println("id"+id )

                        database.getReference("feeds").child(id)
                        val myRef = database.getReference("feeds/"+id)
                        var feed:Feed = Feed(title,des,DownloadURL.toString())
                        myRef.setValue(feed)
                        println("ssss"+ DownloadURL)


                })

        }



        LoadFeeds()

    }


    fun LoadFeeds(){



            var intent = Intent(this, MainActivity::class.java)


            startActivity(intent)

    }






    class Feed {
        var title: String? = null
        var des: String? = null
        var url:String?=null
        constructor() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        constructor(title: String?, des: String?,url:String?) {
            this.title = title
            this.des = des
            this.url = url
        }
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }


}