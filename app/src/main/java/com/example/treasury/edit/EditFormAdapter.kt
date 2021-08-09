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

class EditFormAdapter (private var formArray: ArrayList<Form>, private val haveNote: Boolean) : RecyclerView.Adapter<EditFormAdapter.ViewHolder>() {

    // bind the current cursor and current form
    private var cursor = -1
    private var cursorPlace = -1 // number -> 1, note -> 2
    private var cursorForm: Form? = null

    lateinit var event : Event
    interface Event{
        fun onMoneyChange(number : String, form : Form)
        fun onFormDelete(form : Form)
        fun onNoteChange(note: String, form: Form)
    }

    class ViewHolder(itemView: View, haveNote: Boolean) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.title_edit)
        val number : EditText = itemView.findViewById(R.id.number_edit)
        val deleteButton : ImageButton = itemView.findViewById(R.id.delete_edit)
        var numberWatcher: TextWatcher? = null
        var noteWatcher: TextWatcher? = null
        lateinit var note : EditText
        init {
            if (haveNote){
                note = itemView.findViewById(R.id.note_edit)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int
        if (haveNote){
            layout = R.layout.form_item_edit_note
        }else{
            layout = R.layout.form_item_edit
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ViewHolder(view, haveNote)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Remove any existing TextWatcher that will be keyed to the wrong ListItem
        if(holder.numberWatcher != null){
            holder.number.removeTextChangedListener(holder.numberWatcher)
        }
        if(haveNote && holder.noteWatcher != null){
            holder.note.removeTextChangedListener(holder.noteWatcher)
        }

        val form = formArray[position]
        holder.title.text = (form.name + "：")

        if (holder.number.text.toString() != form.money){
            holder.number.setText(form.money)
        }

        if (haveNote){
            holder.note.setText(form.note)
        }

        // synchronize data when text changes
        holder.numberWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

                // bind the current cursor and current form
                cursor = holder.number.selectionEnd
                cursorForm = form
                cursorPlace = 1

                // synchronize data outside ( view & viewModel )
                event.onMoneyChange(s.toString(), form)
            }
        }
        holder.noteWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

                // bind the current cursor and current form
                cursor = holder.note.selectionEnd
                cursorForm = form
                cursorPlace = 2

                // synchronize data outside ( view & viewModel )
                event.onNoteChange(s.toString(), form)
            }
        }
        holder.number.addTextChangedListener(holder.numberWatcher)
        if (haveNote){
            holder.note.addTextChangedListener(holder.noteWatcher)
        }

        // reset the cursor after update data
        if(form == cursorForm){
            if(cursorPlace == 1){
                holder.number.isFocusable = true
                holder.number.isFocusableInTouchMode = true
                holder.number.requestFocus()
                holder.number.setSelection(cursor)
            }else if(cursorPlace == 2){
                holder.note.isFocusable = true
                holder.note.isFocusableInTouchMode = true
                holder.note.requestFocus()
                holder.note.setSelection(cursor)
            }
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