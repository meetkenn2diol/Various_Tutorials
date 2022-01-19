package com.codelabs.state.viewpager2

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.state.R


class ViewPager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(imageRes: Int) {
        itemView.findViewById<AppCompatImageView>(R.id.image).setImageResource(imageRes)
    }
}