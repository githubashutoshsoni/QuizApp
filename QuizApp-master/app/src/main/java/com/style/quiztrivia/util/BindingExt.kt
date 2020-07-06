package com.style.quiztrivia.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.style.quiztrivia.database.CategoryModel
import com.style.quiztrivia.recycleradapters.CategoryAdapter
import com.style.quiztrivia.ui.category.CategoryViewModel


@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<CategoryModel>?) {
    items?.let {

        if (items.isNotEmpty())
            (listView.adapter as CategoryAdapter).submitList(items)

    }

}


@BindingAdapter("app:imageUrl")
fun setImage(image: ImageView, url: String?) {

    if (!url.isNullOrEmpty()) {
        Glide.with(image.context).load(url).centerCrop().placeholder(ColorDrawable(Color.BLACK))
            .into(image)
    }

}

