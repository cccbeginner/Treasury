package com.example.treasury.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.R
import com.example.treasury.formDatabase.Form

class PageFormAdapter(private var formArray: ArrayList<Form>, private val haveNote: Boolean) : RecyclerView.Adapter<PageFormAdapter.ViewHolder>() {
    class ViewHolder(itemView: View, private val haveNote: Boolean) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.title_show)
        val number : TextView = itemView.findViewById(R.id.number_show)
        lateinit var note : TextView
        init {
            if (haveNote){
                note = itemView.findViewById(R.id.note_show)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int
        if (haveNote){
            layout = R.layout.form_item_show_note
        }else{
            layout = R.layout.form_item_show
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ViewHolder(view, haveNote)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val form = formArray[position]
        holder.title.text = (form.name + "：")
        holder.number.text = form.money
        if (haveNote){
            holder.note.text = form.note
        }
    }

    override fun getItemCount(): Int {
        return formArray.size
    }

    fun updateData(newFormArray: ArrayList<Form>){
        formArray = newFormArray
        notifyDataSetChanged()
    }
}