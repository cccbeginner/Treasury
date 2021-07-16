package com.example.treasury.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.R
import com.example.treasury.formDatabase.Form

class PageFormAdapter(private var formArray: ArrayList<Form>) : RecyclerView.Adapter<PageFormAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.title_page)
        val number : TextView = itemView.findViewById(R.id.number_page)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_item_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val form = formArray[position]
        holder.title.text = (form.name + "：")
        holder.number.text = form.money.toString()
    }

    override fun getItemCount(): Int {
        return formArray.size
    }

    fun updateData(newFormArray: ArrayList<Form>){
        formArray = newFormArray
        notifyDataSetChanged()
    }
}