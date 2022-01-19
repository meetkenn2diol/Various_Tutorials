package com.codelabs.state.viewpager2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.state.R

class ViewPager2Adapter(private val context: Context) : RecyclerView.Adapter<ViewPager2ViewHolder>() {
    // Array of images
    // Adding images from drawable folder
    private val images = intArrayOf(
        R.drawable.ic_baseline_looks_24,
        R.drawable.ic_baseline_looks_one_24,
        R.drawable.ic_baseline_looks_two_24,
        R.drawable.ic_baseline_looks_3_24,
        R.drawable.ic_baseline_looks_4_24,
        R.drawable.ic_baseline_looks_5_24,
        R.drawable.ic_baseline_looks_6_24
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_viewpager2, parent, false)
        return ViewPager2ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPager2ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

}