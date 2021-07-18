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
import com.example.treasury.formDatabase.FormRepository

class EditFormAdapter (private var formArray: ArrayList<Form>) : RecyclerView.Adapter<EditFormAdapter.ViewHolder>() {

    // bind the current cursor and current form
    private var cursor = -1
    private var cursorForm: Form? = null

    lateinit var event : Event
    interface Event{
        fun onMoneyChange(number : Long, form : Form)
        fun onFormDelete(form : Form)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.title_edit)
        val number : EditText = itemView.findViewById(R.id.number_edit)
        val deleteButton : ImageButton = itemView.findViewById(R.id.delete_edit)
        var textWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_item_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Remove any existing TextWatcher that will be keyed to the wrong ListItem
        if(holder.textWatcher != null){
            holder.number.removeTextChangedListener(holder.textWatcher)
        }

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
        holder.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                println(FormRepository.selectedYearMonth)

                // bind the current cursor and current form
                cursor = holder.number.selectionEnd
                cursorForm = form

                // synchronize data outside ( view & viewModel )
                if(s.toString() == ""){
                    event.onMoneyChange(0, form)
                }else{
                    event.onMoneyChange(s.toString().toLong(), form)
                }
            }
        }
        holder.number.addTextChangedListener(holder.textWatcher)

        // reset the cursor after update data
        if(form == cursorForm){
            holder.number.isFocusable = true
            holder.number.isFocusableInTouchMode = true
            holder.number.requestFocus()
            holder.number.setSelection(cursor)
        }

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
        formArray = newFormArray
        notifyDataSetChanged()
    }
}