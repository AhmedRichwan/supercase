package com.rashwan.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_case.view.*
import kotlinx.android.synthetic.main.add_case.view.et10caseModifiedDate
import kotlinx.android.synthetic.main.add_case.view.et1caseNum
import kotlinx.android.synthetic.main.add_case.view.et2caseYear
import kotlinx.android.synthetic.main.add_case.view.et3caseCount
import kotlinx.android.synthetic.main.add_case.view.et4caseCountYear
import kotlinx.android.synthetic.main.add_case.view.et5caseAccuser
import kotlinx.android.synthetic.main.add_case.view.et6caseCreater
import kotlinx.android.synthetic.main.add_case.view.et7caseSessionDate
import kotlinx.android.synthetic.main.add_case.view.et8casePapers
import kotlinx.android.synthetic.main.add_case.view.et9caseNotes
import kotlinx.android.synthetic.main.gshow.*
import kotlinx.android.synthetic.main.restorecase.view.*
import kotlinx.android.synthetic.main.update_case.view.*
import kotlinx.android.synthetic.main.update_case.view.updbtn
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

class nw : AppCompatActivity() {
    var mRef: DatabaseReference? = null
    var mNotelist: ArrayList<Casesinfo>? = null
    val seconds = LocalDateTime.now().atZone(ZoneOffset.systemDefault()).toEpochSecond()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_nw)
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = database.getReference("Cases")
        mNotelist = ArrayList()
        GListView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { p0, p1, p2, p3 ->

                val alertBuilder = AlertDialog.Builder(this)
                var view2 = layoutInflater.inflate(R.layout.activity_cw, null)
                val alertDialog = alertBuilder.create()
                var casy = mNotelist?.get(p2)!!
                if (casy.IsCaseDeleted==1){ mlayout.background= ContextCompat.getDrawable(this, R.drawable.bk)}
                alertDialog.setView(view2)
                alertDialog.show()
                view2.et1caseNum.setText(casy.caseNum)
                view2.et2caseYear.setText(casy.caseYear)
                view2.et3caseCount.setText(casy.caseCount)
                view2.et4caseCountYear.setText(casy.caseCountYear)
                view2.et5caseAccuser.setText(casy.caseAccuser)
                view2.et6caseCreater.setText(casy.caseCreater)
                view2.et7caseSessionDate.setText(casy.caseSessionDate)
                view2.et8casePapers.setText(casy.casePapers)
                view2.et10caseModifiedDate.setText(casy.caseModifiedDate)
                view2.et7caseSessionDate.setOnClickListener() {
                    pickDateValidation(view2.et7caseSessionDate)
                }
                view2.updbtn.setOnClickListener {

                    var childRef = mRef?.child(casy.Id!!)
                    var afterUpdate = Casesinfo(
                        casy.Id!!,
                        view2.et1caseNum.text.toString(),
                        view2.et2caseYear.text.toString(),
                        view2.et3caseCount.text.toString(),
                        view2.et4caseCountYear.text.toString(),
                        view2.et5caseAccuser.text.toString(),
                        view2.et6caseCreater.text.toString(),
                        view2.et7caseSessionDate.text.toString(),
                        view2.et8casePapers.text.toString(),
                        view2.et9caseNotes.text.toString(),
                        (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm"))),
                        0
                    )
                    childRef!!.setValue(afterUpdate)
                    //  if (view2.ite) {
                    Toast.makeText(this, "   تم التعديل والاستعادة", Toast.LENGTH_LONG).show()
                    //}
                    alertDialog.dismiss()
                }
                view2.restorebtn.setOnClickListener {

                    var casy = mNotelist?.get(p2)!!
                    var childRef = mRef?.child(casy.Id!!)
                    var afterDelete = Casesinfo(
                        casy.Id!!,
                        view2.et1caseNum.text.toString(),
                        view2.et2caseYear.text.toString(),
                        view2.et3caseCount.text.toString(),
                        view2.et4caseCountYear.text.toString(),
                        view2.et5caseAccuser.text.toString(),
                        view2.et6caseCreater.text.toString(),
                        view2.et7caseSessionDate.text.toString(),
                        view2.et8casePapers.text.toString(),
                        view2.et9caseNotes.text.toString(),
                        (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm"))),
                        0
                    )

                    childRef!!.setValue(afterDelete)
                    Toast.makeText(this, "تم الاستعادة بنجاح", Toast.LENGTH_LONG).show()

                    alertDialog.dismiss()
                }
                true
            }


        GListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var case: Casesinfo = mNotelist?.get(position)!!
                case.caseNum
                case.caseYear
                case.caseCount
                case.caseCountYear
                case.caseCreater
                case.caseAccuser
                case.casePapers
                case.caseNotes
                case.caseSessionDate
                var caseIntent = Intent(this, caseDetail::class.java)
                caseIntent.putExtra("caseNum", case.caseNum)
                caseIntent.putExtra("caseYear", case.caseYear)
                caseIntent.putExtra("caseCount", case.caseCount)
                caseIntent.putExtra("caseCountYear", case.caseCountYear)
                caseIntent.putExtra("caseCreater", case.caseCreater)
                caseIntent.putExtra("caseAccuser", case.caseAccuser)
                caseIntent.putExtra("casePapers", case.casePapers)
                caseIntent.putExtra("caseNotes", case.caseNotes)
                caseIntent.putExtra("caseSessionDate", case.caseSessionDate)
                caseIntent.putExtra("caseModifiedDate", case.caseModifiedDate)
                startActivity(caseIntent)
            }
    }

    override fun onStart() {
        super.onStart()


        mRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                mNotelist?.clear()

                for (n in p0.children) {
                    val case = n.getValue(Casesinfo::class.java)
                    var checkDeleted = n.child("isCaseDeleted").value
                    var caseSessionDate = n.child("caseSessionDate").value.toString().toLong()
                    val today = LocalDate.now()
                    // Go backward to get Monday
                    var firstdayofweek = today
                    while (firstdayofweek.getDayOfWeek() !== DayOfWeek.SATURDAY)
                    {
                        firstdayofweek = firstdayofweek.minusDays(1)
                    }
                    // Go forward to get Sunday
                    var lastdayofweek = today
                    while (lastdayofweek.getDayOfWeek() !== DayOfWeek.FRIDAY)
                    {
                        lastdayofweek = lastdayofweek.plusDays(1)
                    }

                    val firstDayOfWeek =tools.strToEpoch(firstdayofweek.toString())+604800
                    val lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())+604800

                    if (checkDeleted.toString() == "0" &&(caseSessionDate>=firstDayOfWeek) && (caseSessionDate<=lastDayOfWeek))
                    {
                        mNotelist!!.add(0, case!!)
                    }


                }
                val noteadapter = NotesAdapter(application, mNotelist!!)
                           GListView.adapter=noteadapter
            }
        })


    }




    fun pickDateValidation(textview: TextView) {


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view1, year, month, dayOfMonth ->
                val myCalendar = GregorianCalendar(year, month, dayOfMonth)
                var dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK)
                var daylong =
                    (SimpleDateFormat("EEEE", Locale.getDefault()).format(myCalendar.getTime()))
                var mydateformated = (SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(myCalendar.getTime()))
                val timenowformated =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                if (dayOfWeek > 5) {

                    Toast.makeText(
                        this,
                        "لا يمكن اختيار يوم عطلة ، يرجى التأكد من التاريخ والمحاولة ثانية!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //   var et7: TextView=et7caseSessionDate

                    textview.text = mydateformated
                    //   this.et7caseSessionDate.text = mydateformated

                }


            },
            year,
            month,
            day
        )





        dpd.datePicker.firstDayOfWeek = Calendar.SUNDAY
        dpd.datePicker.minDate = (c.getTimeInMillis())
        c.add(Calendar.YEAR, 1)
        dpd.datePicker.maxDate = (c.getTimeInMillis())

        dpd.show()
        return
    }

}
