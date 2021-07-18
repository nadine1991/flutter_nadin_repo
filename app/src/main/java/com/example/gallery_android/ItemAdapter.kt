package com.example.gallery_android

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.grid_item.view.*
import kotlinx.android.synthetic.main.stag_grid_item.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ItemAdapter(val context: Context, val items: ArrayList<Feed>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    var alllistNotesAdpater = items
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {




        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.stag_grid_item,
                parent,
                false
            )
        )


    }

    fun getFilter(): Filter {

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

            items.clear()
            items.addAll(filterResults?.values as ArrayList<Feed>)
            notifyDataSetChanged()
        }

    }










    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item: Feed = items[position]


        holder.title.text = item.feedtitle
        holder.des.text = item.feeddes
        Picasso.with(context).load(item.feedImageURL).into(holder.image);
        val bundle = Bundle()
        bundle.putString("ID",item.feedID.toString())

      holder.share.setOnClickListener(){
          /////////////////////SHARE
          val image:Bitmap? = getBitmapFromView(holder.image)
          val share = Intent(Intent.ACTION_SEND)
          share.type="image/*"
          share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

          ///////////////////LOAD IMAGEURI FROM STORAGE
          share.putExtra(Intent.EXTRA_STREAM,getLocalBitmapUri(image!!))
          startActivity(context,Intent.createChooser(share,"share via"),bundle)
      }

        holder.itemView.setOnClickListener {

            ////////////////////DELETE
            val bottomSheetFragment = BottomSheetFragment()
            val bundle = Bundle()

            bundle.putString("ID",item.feedID.toString())
            bundle.putString("image_url",item.feedImageURL.toString())
            bundle.putString("feed_des",item.feeddes.toString())
            bundle.putString("feed_title",item.feedtitle.toString())

            bundle.putSerializable("ListFeeeds",items)

            bottomSheetFragment.arguments = bundle



            bottomSheetFragment.show(
                (context as AppCompatActivity).supportFragmentManager,
                "bottomSheetFragment"
            )

        }





        println("itemfeed"+item.feedtitle)

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.tvItem.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.lightPink
                )
            )
        } else {
            holder.tvItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }
    }


    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
           // bmpUri = Uri.fromFile(file)
            val photoURI = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    fun getBitmapFromView(view: ImageView):Bitmap?{

        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap

    }












    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
       val tvItem = view.stag_layout
        val share = view.share
        val title = view.stag_Title
        val des = view.stag_Des
        val image = view.stag_img
    }
}