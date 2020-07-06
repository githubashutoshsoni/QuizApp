package com.style.quiztrivia.recycleradapters

import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.style.quiztrivia.database.CategoryModel
import com.style.quiztrivia.databinding.LayoutListBinding

class CategoryAdapter : ListAdapter<CategoryModel, VH>() {


    class ViewHolder private constructor(val binding: LayoutListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryModel) {

            binding.mythBtn

        }

    }

}


class SimpleCategoryDiffUtil : DiffUtil.ItemCallback<CategoryModel>() {
    override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {

        return oldItem == newItem
    }

}
