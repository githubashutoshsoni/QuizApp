package com.style.quiztrivia.recycleradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.style.quiztrivia.database.CategoryModel
import com.style.quiztrivia.databinding.ItemCategoryBinding
import com.style.quiztrivia.recycleradapters.CategoryAdapter.ViewHolder
import com.style.quiztrivia.ui.category.CategoryViewModel


class CategoryAdapter(private val viewmodel: CategoryViewModel) :
    ListAdapter<CategoryModel, ViewHolder>(SimpleCategoryDiffUtil()) {


    class ViewHolder private constructor(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewmodel: CategoryViewModel, item: CategoryModel) {

            binding.viewmodel = viewmodel
            binding.category = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewmodel, item)

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
