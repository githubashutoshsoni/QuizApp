package com.example.quizapp.recycleradapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import kotlinx.android.synthetic.main.item_options_layout.view.*

class OptionsAdapter : RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {


    var choiceList: MutableList<String>? = null


    fun setChoicelist(mutableList: MutableList<String>?) {
        this.choiceList = mutableList
        selectedOption = -1;
        notifyDataSetChanged()
    }

    var selectedOption: Int = -1;


    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {

        holder.checkBoxView?.text = choiceList?.get(position);
        Log.d(TAG, "$position  selected$selectedOption")




        if (position == selectedOption) {


            holder.checkBoxView.isChecked = true
//            holder.checkBoxView.setBackgroundColor(
//                ContextCompat.getColor(holder.checkBoxView.context, R.color.colorAccent)
//            )

            holder.checkBoxView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.left_arrow_options,
                0
            )

            holder.parentLayout.setBackgroundColor(
                ContextCompat.getColor(holder.checkBoxView.context, R.color.colorAccent)
            )

        } else {
            holder.checkBoxView.isChecked = false


            holder.checkBoxView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
//            holder.checkBoxView.setBackgroundColor(
//                ContextCompat.getColor(holder.checkBoxView.context, R.color.colorSurface)
//            )


            holder.parentLayout.setBackgroundColor(
                ContextCompat.getColor(holder.checkBoxView.context, R.color.colorOptionsSurface)
            )


        }

    }

    private val TAG = OptionsAdapter::class.java.simpleName

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_options_layout, parent, false)
        return OptionsViewHolder(view)

    }


    var onItemClick: ((String) -> Unit)? = null

    inner class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBoxView: CheckBox = itemView.choice_chip
        val parentLayout: LinearLayout = itemView.parent_layout

        init {


            checkBoxView.setOnClickListener {


                Log.d(TAG, "selected pos $selectedOption")
                selectedOption = adapterPosition


//              onItemClick?.invoke(choiceList?.get(adapterPosition))


                choiceList?.get(adapterPosition)?.let { it1 -> onItemClick?.invoke(it1) };

                notifyDataSetChanged()
            }


        }


    }

    override fun getItemCount(): Int {
        return choiceList?.size ?: 0
    }


    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int, viewId: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType, it.id)
        }
        return this
    }


}

