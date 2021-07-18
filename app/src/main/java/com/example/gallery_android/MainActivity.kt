package com.example.gallery_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


//import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {



    private var database= FirebaseDatabase.getInstance()
    var ListFeeds=ArrayList<Feed>()
    var displayFeeds=ArrayList<Feed>()


    var adpater:MyFeedAdpater?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ListFeeds.add(Feed("0","him","url","add"))
        val database = FirebaseDatabase.getInstance()


        adpater= MyFeedAdpater(this,ListFeeds)


        val gvListImages: GridView = this.findViewById(R.id.gvItemsList)
        gvListImages.adapter=adpater



        LoadFeed()



        Switch2.setOnClickListener { view ->

                val intent = Intent(this, Staggered_recycleview::class.java)
                startActivity(intent)

        }





    }




    inner class  MyFeedAdpater: BaseAdapter , Filterable{
        var alllistNotesAdpater=ArrayList<Feed>()
        var listNotesAdpater=ArrayList<Feed>()
        var context:Context?=null
        constructor(context:Context, listNotesAdpater:ArrayList<Feed>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.alllistNotesAdpater=listNotesAdpater
            this.context=context
        }




        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {


            var myfeed=listNotesAdpater[p0]


                var myView = layoutInflater.inflate(R.layout.grid_item, null)

                val title = myView.findViewById(R.id.Title) as TextView
                val des = myView.findViewById(R.id.Des) as TextView
                val imageview = myView.findViewById(R.id.SrcImage) as ImageView

                title.text = myfeed.feedtitle
                des.text = myfeed.feeddes



            Picasso.with(applicationContext).load(myfeed.feedImageURL).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.camera).into(imageview , object : Callback {
                    override fun onSuccess() {

                    print("Success Caching")
                    }
                    override fun onError() {
                        Picasso.with(applicationContext).load(myfeed.feedImageURL).placeholder(R.drawable.camera)
                            .into(imageview)
                    }
                })
                return myView


        }



        override fun getItem(p0: Int): Any {
            return listNotesAdpater[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {

            return listNotesAdpater.size

        }

        override fun getFilter(): Filter {

            return filter
        }

        private val filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredList = ArrayList<Feed>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(alllistNotesAdpater)
                } else {
                    for (item in alllistNotesAdpater) {
                        if (item.feedtitle?.toLowerCase()?.startsWith(constraint.toString().toLowerCase()) == true) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {

                listNotesAdpater.clear()
                listNotesAdpater.addAll(filterResults?.values as ArrayList<Feed>)
                notifyDataSetChanged()
            }

        }

    }




    fun LoadFeed(){
        var ref: DatabaseReference = database.getReference().child("feeds")

        println("ref" + ref)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                println("hello "+dataSnapshot )

                ListFeeds.clear()

                        var td= dataSnapshot!!.value as HashMap<String,Any>
                        println("td" + td)
                        for(key in td.keys) {

                            var post = td[key] as HashMap<String, Any>
                            println("key" + key)
                            println("title" + post["title"] as String)
                            println("des" + post["des"] as String)
                            println("url" + post["url"] as String)
                            ListFeeds.add(
                                Feed(
                                    key,

                                    post["title"] as String,
                                    post["url"] as String,
                                    post["des"] as String,

                                )
                            )


                        }
                println("list of feeds"+ListFeeds[4].feedtitle)

                adpater!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

    }

    fun filter(text: String?) {
        val temp: ArrayList<Feed> = ArrayList()

        for (d in ListFeeds) {

            print("search "+d)

            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.equals(text)) {

                temp.add(d)
            }
        }
        //update recyclerview

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val search = menu?.findItem(R.id.Search)

        val searchView = search?.actionView as SearchView

        searchView.queryHint = "Search Something!"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?):Boolean {
                adpater?.getFilter()?.filter(p0);

                return false
            }


        })




        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item!=null){
            print(item)
           when(item.itemId)

            {
            R.id.addImage->{
                print(item.itemId)
                var intent= Intent(this,Add_Image::class.java)
                startActivity(intent)
            }
           }
        }





        return super.onOptionsItemSelected(item)
    }






}