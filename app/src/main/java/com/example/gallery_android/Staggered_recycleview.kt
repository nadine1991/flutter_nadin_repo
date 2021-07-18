package com.example.gallery_android

import android.R.attr.data
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.*
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_staggered_recycleview.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Staggered_recycleview : AppCompatActivity() {
    var offset:Long = 0
    var isCounterRunning:Boolean = true
    private var database= FirebaseDatabase.getInstance()
    var collection_feeds:Collection_Feeds?= null
    var timer:CountDownTimer?= null
    var ListFeeds=ArrayList<Feed>()
    var adapter: ItemAdapter?=null
    var recyclerView: RecyclerView? = null
    var emptyview:TextView? = null
    var length_deleted_items = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staggered_recycleview)


        timer = object: CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {


            }

            override fun onFinish() {

            println("timer has finished")
            isCounterRunning = false;
            println("ListFeeds.size "+ ListFeeds.size)
            var ref: DatabaseReference = database.getReference().child("feeds")

            if(ListFeeds.size > 10 ){
                //////////////////DELETE
                length_deleted_items =  ListFeeds.size - 10
                println("length_deleted_items  " + length_deleted_items)

                var index = ListFeeds.size - 1
                while(length_deleted_items > 0 ){
                   var deletedid =  ListFeeds[index].feedID
                    println("deletedid  " +deletedid)
                    ListFeeds.removeAt(index)
                    ref.child(deletedid.toString()).removeValue()
                    println("last" + ref.limitToLast(1).get())
                index = index - 1
                length_deleted_items = length_deleted_items -1


                    ////////////////GET CURRENT TIME
                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val currentDate = sdf.format(Date())
                    var deleted_time = currentDate

                /////////////SAVE TO TRASH
                    var id  =  getRandomString(9)
                    database.getReference("trash").child(id)
                    val myRef = database.getReference("trash/"+id)
                    var trashed: Deleted_Item = Deleted_Item(id,ListFeeds[index].feedtitle.toString(),
                        ListFeeds[index].feeddes.toString(), ListFeeds[index].feedImageURL.toString(),deleted_time.toString() )
                    myRef.setValue(trashed)
                    // println("ssss"+ DownloadURL)




                }

            }





            start_again()
            }


        }.start()



        LoadFeed()




        Switch2.setOnClickListener { view ->

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }




        recyclerView = findViewById(R.id.recycleViewStagged)
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)






    }


    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }



    private fun start_again() {
        isCounterRunning = true;
        timer?.start();
    }


    object simpleCallback : ItemTouchHelper.Callback() {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) : Boolean{


            recyclerView.adapter!!.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            return false;
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }


    }








    private fun stag_grid() {


        recycleViewStagged.setItemAnimator(SlideInUpAnimator())
        // Set the LayoutManager that this RecyclerView will use.
        recycleViewStagged.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Adapter class is initialized and list is passed in the param.
        println("is empty stag"+ListFeeds.isEmpty())
        adapter = ItemAdapter(this, ListFeeds)
        // adapter instance is set to the recyclerview to inflate the items.
        recycleViewStagged.adapter = adapter
    }



    fun LoadFeed() {
        var ref: DatabaseReference = database.getReference().child("feeds")

        println("ref" + ref)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //var td= dataSnapshot!!.value as HashMap<String,Any>
                // println("td" + td)

                println("hello " + dataSnapshot)

                ListFeeds.clear()
                //ListTweets.add(Ticket("0","him","url","add"))
                //ListFeeds.add(Feed("5", "tested", "url", "ads"))
                var td = dataSnapshot!!.value as HashMap<String, Any>
                println("td" + td)
                for (key in td.keys) {

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
                println("list of feeds" + ListFeeds[4].feedtitle)

                recyclerView = findViewById(R.id.recycleViewStagged)
                emptyview = findViewById(R.id.empty_view)
                adapter?.notifyDataSetChanged()


                //ListFeeds.clear()
                if(!ListFeeds.isEmpty())
                {
                    println("is empty "+ListFeeds.isEmpty())

                    emptyview?.run { setVisibility(View.INVISIBLE) }
                    recyclerView?.run {setVisibility(View.VISIBLE)  }
                    stag_grid()
                }
                else{

                    println("is empty "+ListFeeds.isEmpty())
                    emptyview.run { this?.setVisibility(View.VISIBLE) }
                    recyclerView?.run {setVisibility(View.INVISIBLE)  }
                }
                //ListFeeds.clear()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

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

                adapter?.getFilter()?.filter(p0)
                return false
            }


        })
















        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null){

            when(item.itemId) {
                R.id.addImage->{
                    var intent= Intent(this,Add_Image::class.java)
                    startActivity(intent)
                }
                R.id.trash_icon->{
                    var intent= Intent(this,Trash::class.java)
                    startActivity(intent)
                }
            }
        }




        return super.onOptionsItemSelected(item)
    }
}

