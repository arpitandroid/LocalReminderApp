package com.example.reminderapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView


class AdapterReminders(var allReminders: MutableList<Reminders>, var mainActivity: MainActivity) :
    RecyclerView.Adapter<AdapterReminders.MyViewHolder?>() {

    private var message: TextView? = null
    private var time: TextView? = null
    private var desc: TextView? = null
    private var edit: TextView? = null
    private var delete: TextView? = null
    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.reminder_item, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        val reminders = allReminders[i]
        if (reminders.getTitle() != "") message!!.text =
            "TITLE : "+reminders.getTitle() else message!!.hint =
            "No title"

        if (reminders.description != "") desc!!.text =
            "DESC : "+reminders.description else desc!!.hint =
            "No desc"

        time!!.text = reminders.getRemindDate().toString()

        delete?.setOnClickListener {
            Log.d("aj"," pos=> $i")
            try {
                allReminders.removeAt(i)
                notifyDataSetChanged()
            }catch (e:Exception){
                Log.d("aj"," exc=> $e")
            }


        }

        edit?.setOnClickListener {

            (mainActivity as MainActivity).addReminder(allReminders[i].getId())


        }

    }

    override fun getItemCount(): Int {
        return allReminders.size
    }

    inner class MyViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        init {
            message = itemView.findViewById(R.id.textView1)
            desc = itemView.findViewById(R.id.textView3)
            time = itemView.findViewById(R.id.textView2)
            edit = itemView.findViewById(R.id.tv_edit)
            delete = itemView.findViewById(R.id.tv_delete)


        }
    }

}