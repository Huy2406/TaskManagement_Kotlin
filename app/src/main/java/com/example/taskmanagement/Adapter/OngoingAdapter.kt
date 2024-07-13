package com.example.taskmanagement.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskmanagement.Domain.OngoingDomain
import com.example.taskmanagement.R


class OngoingAdapter(private val item: List<OngoingDomain>):
    RecyclerView.Adapter<OngoingAdapter.Viewholder>() {
    inner class Viewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val title:TextView=itemView.findViewById(R.id.titleTxt)
        val data:TextView=itemView.findViewById(R.id.dateTxt)
        val progressBarPercent:TextView=itemView.findViewById(R.id.percentTxt)
        val progressTxt:TextView=itemView.findViewById(R.id.progressTxt)
        val progressBar:ProgressBar=itemView.findViewById(R.id.progressBar)
        val pic:ImageView=itemView.findViewById(R.id.titleTxt)
        val layout:ConstraintLayout=itemView.findViewById(R.id.layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_ongoing, parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
    val item=item[position]
        holder.title.text=item.title
        holder.title.text=item.data
        holder.progressBarPercent.text="${item.progressPercent}%"

        val drawableResourceId=holder.itemView.context.resources.getIdentifier(
            item.picPath, "drawable", holder.itemView.context.packageName
        )

        Glide.with(holder.itemView.context).load(drawableResourceId).into(holder.pic)

    }
}