package com.example.gallery_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.stag_grid_item.view.*
import kotlinx.android.synthetic.main.trash_item.view.*

class Trash_Adapter(val context: Context, val items: ArrayList<Deleted_Item>) :
    RecyclerView.Adapter<Trash_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Trash_Adapter.ViewHolder {
        return Trash_Adapter.ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.trash_item,
                parent,
                false
            )
        )



    }



    override fun getItemCount(): Int {
        return items.size
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
       // val tvItem = view.stag_layout
        val trash_item_view = view.trash_item_view
        var Title = view.tvTitle
        var Des = view.tvDes
        var Time = view.tvTime
        var Image = view.ivImage

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Deleted_Item= items[position]

        holder.Title.text = item.feedtitle
        holder.Des.text = item.feeddes
        Picasso.with(context).load(item.feedImageURL).into(holder.Image);
        holder.Time.text = item.deleted_time





        if (position % 2 == 0) {
            holder.trash_item_view.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGray
                )
            )
        }





    }














}