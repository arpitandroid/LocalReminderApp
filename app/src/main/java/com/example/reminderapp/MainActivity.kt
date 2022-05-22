package com.example.reminderapp

import android.app.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.reminderapp.databinding.ActivityMainBinding
import android.util.Log
import android.view.inputmethod.InputMethodManager
import java.lang.Exception

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.view.WindowManager.LayoutParams.*
import android.widget.*

import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager

import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding

    private var dialog: Dialog? = null

    private var adapter: AdapterReminders? = null

    var datalist= ArrayList<Reminders>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingButton.setOnClickListener(View.OnClickListener { addReminder(0) })

       binding.recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.layoutManager = linearLayoutManager

    }


    private fun hideSoftKeyboard(activity: Activity) {

        try {
            val inputMethodManager: InputMethodManager =
                activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }catch (e:Exception){
            Log.d("==aj==","ex=> $e")
        }

    }

    fun addReminder(id: Int) {
        dialog = Dialog(this@MainActivity)
        dialog!!.setContentView(R.layout.popupview)
        dialog!!.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        val title: TextView = dialog!!.findViewById<TextView>(R.id.et_title) // date
        val description: TextView = dialog!!.findViewById<TextView>(R.id.id_desc)
        val date: TextView = dialog!!.findViewById<TextView>(R.id.tv_timepicker)
        val add: AppCompatButton = dialog!!.findViewById<AppCompatButton>(R.id.save)

        if(id !=0){
            Log.d("aj", "=id= $id")
            datalist.forEach { event ->
                if (event.id == event.id) {
                    title.text = event.getTitle()
                    description.text = event.Description
                    date.text = event.remindDate.toString()

                }
            }
        }

        val newCalender = Calendar.getInstance()
        date.setOnClickListener {
            val dialog = DatePickerDialog(
                this@MainActivity,
                { _, year, month, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    val newTime = Calendar.getInstance()
                    val time = TimePickerDialog(
                        this@MainActivity,
                        { _, hourOfDay, minute ->
                            newDate[year, month, dayOfMonth, hourOfDay, minute] = 0
                            val tem = Calendar.getInstance()
                            Log.w(
                                "TIME",
                                System.currentTimeMillis().toString() + ""
                            )
                            if (newDate.timeInMillis - tem.timeInMillis > 0) date.text =
                                newDate.time.toString() else Toast.makeText(
                                this@MainActivity,
                                "Invalid time",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        newTime[Calendar.HOUR_OF_DAY],
                        newTime[Calendar.MINUTE], true
                    )
                    time.show()
                },
                newCalender[Calendar.YEAR],
                newCalender[Calendar.MONTH], newCalender[Calendar.DAY_OF_MONTH]
            )
            dialog.datePicker.minDate = System.currentTimeMillis()
            dialog.show()
        }

        add.setOnClickListener {

        when {
            title.text!!.isEmpty() -> {
                Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show()
            }
            description.text!!.isEmpty() -> {
                Toast.makeText(this, "Enter Discription", Toast.LENGTH_SHORT).show()
            }
            date.text!!.isEmpty() -> {
                Toast.makeText(this, "Enter Discription", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Fields Are Validated", Toast.LENGTH_SHORT).show()

            var reminders = Reminders()
            reminders.setId((100..200).random())
            reminders.setTitle(title.text.toString().trim { it <= ' ' })
            reminders.description = description.text.toString().trim { it <= ' ' }
            val remind = Date(date.text.toString().trim { it <= ' ' })
            reminders.setRemindDate(remind)
                datalist.add(reminders)

            val l: List<Reminders> = datalist
            reminders = l[l.size - 1]
            Log.e("ID null", reminders.getId().toString() + "")
            val calendar =
                Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"))
            calendar.time = remind
            calendar[Calendar.SECOND] = 0
            val intent = Intent(this@MainActivity, NotifyAlarm::class.java)
            intent.putExtra("Message", reminders.getTitle())
            intent.putExtra("desc", reminders.description)
            intent.putExtra("RemindDate", reminders.getRemindDate().toString())
            intent.putExtra("id", reminders.getId())
            val intent1 = PendingIntent.getBroadcast(
                this@MainActivity,
                reminders.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent1)
            Toast.makeText(this@MainActivity, "Inserted Successfully", Toast.LENGTH_SHORT).show()
            setItemsInRecyclerView()
           // AppDatabase.destroyInstance()
            dialog!!.dismiss()
            }
          }
        }
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.show()
    }

    private fun setItemsInRecyclerView() {

        if (datalist.isNotEmpty()) {
            binding.empty.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        }
        adapter = AdapterReminders(datalist,this)
        binding.recyclerView.adapter = adapter
    }

}