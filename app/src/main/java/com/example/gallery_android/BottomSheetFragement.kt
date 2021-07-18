package com.example.gallery_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetFragment : BottomSheetDialogFragment() {
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference
    var adapter: ItemAdapter?=null
    var collection_feeds:Collection_Feeds?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.bottom_sheet,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mArgs = arguments

        val ID = mArgs!!.getString("ID")
        val image_url = mArgs!!.getString("image_url")
        val feed_des = mArgs!!.getString("feed_des")
        val feed_title = mArgs!!.getString("feed_title")


        button1.setOnClickListener(){

            println("Bottom_ListFeeeds_ID "+ ID)


           myRef.child("feeds").child( ID.toString()).removeValue()

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            var deleted_time = currentDate

            ///////SAVE TO TRASH
            var id  =  getRandomString(9)
            database.getReference("trash").child(id)
            val myRef = database.getReference("trash/"+id)
            var trashed: Deleted_Item = Deleted_Item(id,feed_title.toString(),
                feed_des.toString(), image_url.toString() ,deleted_time.toString(),)
            myRef.setValue(trashed)
           // println("ssss"+ DownloadURL)





           this.dismiss();

        }
        button2.setOnClickListener(){

            this.dismiss();

        }
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}