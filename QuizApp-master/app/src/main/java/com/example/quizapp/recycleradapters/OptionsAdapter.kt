package com.example.quizapp.recycleradapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
        holder.checkBoxView.isChecked = position == selectedOption

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

        val checkBoxView: RadioButton = itemView.choice_chip

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

