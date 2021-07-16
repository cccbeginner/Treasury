package com.example.treasury.edit

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.R
import com.example.treasury.formDatabase.Form

class EditFormAdapter (private var formArray: ArrayList<Form>) : RecyclerView.Adapter<EditFormAdapter.ViewHolder>() {

    lateinit var event : Event
    interface Event{
        fun onMoneyChange(number : Long, form : Form)
        fun onFormDelete(form : Form)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.title_edit)
        val number : EditText = itemView.findViewById(R.id.number_edit)
        val deleteButton : ImageButton = itemView.findViewById(R.id.delete_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_item_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val form = formArray[position]
        holder.title.text = (form.name + "：")

        if (holder.number.text.toString() != form.money.toString()){
            if(form.money != 0L) {
                holder.number.setText(form.money.toString())
            }else if (holder.number.text.toString().isNotEmpty()){
                holder.number.setText("")
            }
        }

        // synchronize data when text changes
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == ""){
                    event.onMoneyChange(0, form)
                }else {
                    event.onMoneyChange(s.toString().toLong(), form)
                }
            }
        }
        holder.number.addTextChangedListener(textWatcher)

        // delete form
        holder.deleteButton.setOnClickListener {
            event.onFormDelete(form)
        }
    }

    override fun getItemCount(): Int {
        return formArray.size
    }

    // BUG PART
    fun updateData(newFormArray: ArrayList<Form>){
        println("Update Data")
        println("Old data : " + formArray)
        formArray = newFormArray
        println("New data : " + formArray)
        notifyDataSetChanged()
    }
}