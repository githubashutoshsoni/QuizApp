package com.example.quizapp.recycleradapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import java.util.zip.Inflater

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val materialCard

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list, parent, false);
        return CategoryViewHolder(view)

    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    }
}