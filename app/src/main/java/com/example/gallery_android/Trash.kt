package com.example.gallery_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import java.util.*
import kotlin.collections.ArrayList


class Trash : AppCompatActivity() {


    var ListTrash=ArrayList<Deleted_Item>()
    var adapter: Trash_Adapter?=null
    var list_trash: RecyclerView? = null
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)


        list_trash = findViewById(R.id.list_trash) as RecyclerView

        LoadTrash()

        List_grid()





    }






















    private fun List_grid() {


        list_trash?.setItemAnimator(SlideInUpAnimator())
        // Set the LayoutManager that this RecyclerView will use.
        list_trash?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // Adapter class is initialized and list is passed in the param.
        //println("is empty stag"+ListFeeds.isEmpty())
        adapter = Trash_Adapter(this, ListTrash)
        // adapter instance is set to the recyclerview to inflate the items.
        list_trash?.adapter = adapter
    }




















    private fun LoadTrash() {
        var ref: DatabaseReference = database.getReference().child("trash")

        println("ref" + ref)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                println("hello " + dataSnapshot)

                ListTrash.clear()

                var td = dataSnapshot!!.value as HashMap<String, Any>
                println("td" + td)
                for (key in td.keys) {

                    var post = td[key] as HashMap<String, Any>
                    //println("key" + key)
                   // println("title" + post["title"] as String)
                    //println("des" + post["des"] as String)
                   // println("url" + post["url"] as String)
                    ListTrash.add(
                        Deleted_Item(
                            key,
                            post["feedtitle"] as String,
                            post["feeddes"] as String,
                            post["feedImageURL"] as String,
                            post["deleted_time"] as String,
                            )
                    )


                }
              //  println("list of feeds" + ListFeeds[4].feedtitle)


                adapter?.notifyDataSetChanged()





                var callback_delete = ItemTouchHelper(
                    object : ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT
                    ) {


                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            TODO("Not yet implemented")
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            ListTrash.removeAt(viewHolder.adapterPosition)

                            var id =  ListTrash[viewHolder.adapterPosition].feedID
                            myRef.child("trash").child(id.toString()).removeValue()


                            adapter?.notifyDataSetChanged()
                        }


                    })


                callback_delete.attachToRecyclerView(list_trash)

            }



            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

    }





}