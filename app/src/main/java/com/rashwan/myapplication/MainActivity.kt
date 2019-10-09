package com.rashwan.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.update_case.view.*
//import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    var mRef: DatabaseReference? = null
    var mNotelist: ArrayList<Casesinfo>? = null


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = database.getReference("Cases")
        mNotelist = ArrayList()
        addfltbtn.setOnClickListener { showdialogeAddnote() }


        new_list_view.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { p0, p1, p2, p3 ->
                var sortedList = mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()
                var casy = sortedList?.get(p2)!!

                val alertBuilder = AlertDialog.Builder(this)
//                var update_case_view = layoutInflater.inflate(R.layout.update_case, null)
//                if (casy.IsCaseDeleted==0){
//                    update_case_view = layoutInflater.inflate(R.layout.update_case, null)

                var update_case_view = layoutInflater.inflate(R.layout.edit_case, null)



                val alertDialog = alertBuilder.create()

                alertDialog.setView(update_case_view)
                alertDialog.show()
                update_case_view.et1caseNum.setText(casy.caseNum)
                update_case_view.et2caseYear.setText(casy.caseYear)
                update_case_view.et3caseCount.setText(casy.caseCount)
                update_case_view.et4caseCountYear.setText(casy.caseCountYear)
                update_case_view.et5caseAccuser.setText(casy.caseAccuser)
                update_case_view.et6caseCreater.setText(casy.caseCreater)
                update_case_view.et7caseSessionDate.text =
                    tools.epochToStr((casy.caseSessionDate)!!.toLong())
                update_case_view.et8casePapers.setText(casy.casePapers)
                update_case_view.et9caseNotes.setText(casy.caseNotes)
                update_case_view.et10caseModifiedDate.setText(casy.caseModifiedDate)
                update_case_view.et7caseSessionDate.setOnClickListener {
                    pickDateValidation(update_case_view.et7caseSessionDate)
                }


                update_case_view.updbtn.setOnClickListener {

                    var childRef = mRef?.child(casy.Id!!)

                    var afterUpdate = Casesinfo(
                        casy.Id!!,
                        update_case_view.et1caseNum.text.toString(),
                        update_case_view.et2caseYear.text.toString(),
                        update_case_view.et3caseCount.text.toString(),
                        update_case_view.et4caseCountYear.text.toString(),
                        update_case_view.et5caseAccuser.text.toString(),
                        update_case_view.et6caseCreater.text.toString(),
                        tools.strToEpoch(update_case_view.et7caseSessionDate.text.toString()).toString(),
                        update_case_view.et8casePapers.text.toString(),
                        update_case_view.et9caseNotes.text.toString(),
                        (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm"))),
                        0
                    )
                    childRef!!.setValue(afterUpdate)
                    //  if (view2.ite) {
                    Toast.makeText(this, "تم التعديل", Toast.LENGTH_LONG).show()
                    //}
                    alertDialog.dismiss()
                }
                update_case_view.delbtn.setOnClickListener {

                    var casy = mNotelist?.get(p2)!!
                    var childRef = mRef?.child(casy.Id!!)
                    var toast: String? = null
                    var casedeletionstate = 0
                    if (casy.IsCaseDeleted == 0) {
                        casedeletionstate = 1
                        toast = "تم الحذف بنجاح"
                    } else {
                        casedeletionstate = 0

                        toast = "تم الاسترجاع بنجاح"


                    }
                    var afterDelete = Casesinfo(
                        casy.Id!!,
                        update_case_view.et1caseNum.text.toString(),
                        update_case_view.et2caseYear.text.toString(),
                        update_case_view.et3caseCount.text.toString(),
                        update_case_view.et4caseCountYear.text.toString(),
                        update_case_view.et5caseAccuser.text.toString(),
                        update_case_view.et6caseCreater.text.toString(),
                        tools.strToEpoch(update_case_view.et7caseSessionDate.text.toString()).toString(),
                        update_case_view.et8casePapers.text.toString(),
                        update_case_view.et9caseNotes.text.toString(),
                        (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm"))),
                        casedeletionstate
                    )

                    childRef!!.setValue(afterDelete)

                    Toast.makeText(this, " $toast", Toast.LENGTH_LONG).show()

                    alertDialog.dismiss()
                }
                true
            }


        new_list_view.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                var sortedList = mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()
                var case = sortedList?.get(position)!!
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

        var sortedList =
            mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()

        val noteadapter = NotesAdapter(application, sortedList!!)

//        RV.adapter= SimpleAdapter()
}


    fun LVA(view: View) {
        when {
            view.id == (R.id.cwbtn) -> mRef?.addValueEventListener(object : ValueEventListener {
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
                        while (firstdayofweek.dayOfWeek !== DayOfWeek.SATURDAY) {
                            firstdayofweek = firstdayofweek.minusDays(1)
                        }
                        // Go forward to get Sunday
                        var lastdayofweek = today
                        while (lastdayofweek.dayOfWeek !== DayOfWeek.FRIDAY) {
                            lastdayofweek = lastdayofweek.plusDays(1)
                        }

                        val firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString())
                        val lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())

                        if (checkDeleted.toString() == "0" && (caseSessionDate >= firstDayOfWeek) && (caseSessionDate <= lastDayOfWeek)) {
                            mNotelist!!.add(0, case!!)
                        }


                    }
//
                    var sortedList =
                        mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()

                    val noteadapter = NotesAdapter(application, sortedList!!)
                    new_list_view.adapter = noteadapter
                }
            })
            view.id == (R.id.nwbtn) -> mRef?.addValueEventListener(object : ValueEventListener {
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
                        while (firstdayofweek.dayOfWeek !== DayOfWeek.SATURDAY) {
                            firstdayofweek = firstdayofweek.minusDays(1)
                        }
                        // Go forward to get Sunday
                        var lastdayofweek = today
                        while (lastdayofweek.dayOfWeek !== DayOfWeek.FRIDAY) {
                            lastdayofweek = lastdayofweek.plusDays(1)
                        }

                        val firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString()) + 604800
                        val lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString()) + 604800

                        if (checkDeleted.toString() == "0" && (caseSessionDate >= firstDayOfWeek) && (caseSessionDate <= lastDayOfWeek)) {
                            mNotelist!!.add(0, case!!)
                        }


                    }
                    var sortedList =
                        mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()

                    val noteadapter = NotesAdapter(application, sortedList!!)
                    new_list_view.adapter = noteadapter
                }

            })
            view.id == (R.id.allbtn) -> mRef?.addValueEventListener(object : ValueEventListener {
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
                        while (firstdayofweek.dayOfWeek !== DayOfWeek.SATURDAY) {
                            firstdayofweek = firstdayofweek.minusDays(1)
                        }
                        // Go forward to get Sunday
                        var lastdayofweek = today
                        while (lastdayofweek.dayOfWeek !== DayOfWeek.FRIDAY) {
                            lastdayofweek = lastdayofweek.plusDays(1)
                        }

                        val firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString()) + 604800
                        val lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString()) + 604800

                        if (checkDeleted.toString() == "0")//&&(caseSessionDate>=firstDayOfWeek) && (caseSessionDate<=lastDayOfWeek))
                        {
                            mNotelist!!.add(0, case!!)
                        }


                    }

//                    var sortedList = mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()
//                    val noteadapter = NotesAdapter(application, sortedList!!)
                    var sortedList =
                        mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()

                    val noteadapter = NotesAdapter(application, sortedList!!)
                    new_list_view.adapter = noteadapter
                }

            })
            view.id == (R.id.delbtn) -> mRef?.addValueEventListener(object : ValueEventListener {
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
                        while (firstdayofweek.dayOfWeek !== DayOfWeek.SATURDAY) {
                            firstdayofweek = firstdayofweek.minusDays(1)
                        }
                        // Go forward to get Sunday
                        var lastdayofweek = today
                        while (lastdayofweek.dayOfWeek !== DayOfWeek.FRIDAY) {
                            lastdayofweek = lastdayofweek.plusDays(1)
                        }

                        val firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString()) + 604800
                        val lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString()) + 604800

                        if (checkDeleted.toString() == "1")//&&(caseSessionDate>=firstDayOfWeek) && (caseSessionDate<=lastDayOfWeek))
                        {
                            mNotelist!!.add(0, case!!)
                        }


                    }
                    var sortedList =
                        mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()
                    val noteadapter = NotesAdapter(application, sortedList!!)
                    new_list_view.adapter = noteadapter
                }

            })
        }
}

    fun showdialogeAddnote() {
        val alertbuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_case, null)

        alertbuilder.setView(view)
        val alertDialoge = alertbuilder.create()
        alertDialoge.show()
        val view1 = (R.layout.add_case)
        // R.layout.add_case.


        view.et7caseSessionDate.setOnClickListener {
            pickDateValidation(view.et7caseSessionDate)

        }


        view.addbtn.setOnClickListener {

            if (view.et1caseNum.text.toString().isNotEmpty() && view.et2caseYear.text.toString().isNotEmpty() && view.et7caseSessionDate.text.toString().isNotEmpty()) {


                val id = mRef!!.push().key!!
                val myCase = Casesinfo(
                    id,
                    view.et1caseNum.text.toString(),
                    view.et2caseYear.text.toString(),
                    view.et3caseCount.text.toString(),
                    view.et4caseCountYear.text.toString(),
                    view.et5caseAccuser.text.toString(),
                    view.et6caseCreater.text.toString(),
                    tools.strToEpoch(view.et7caseSessionDate.text.toString()).toString() ,
                    view.et8casePapers.text.toString(),
                    view.et9caseNotes.text.toString(),
                    view.et10caseModifiedDate.text.toString() + (LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm")
                    )),
                    0
                )
                mRef!!.child(id).setValue(myCase)
            var testfun = tools.strToEpoch("2010-05-01").toString()
                Toast.makeText(this, testfun, Toast.LENGTH_LONG).show()
                var sortedList = mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()
                val noteadapter = NotesAdapter(application, sortedList!!)
                new_list_view.adapter=noteadapter

                //   Toast.makeText(this, "تم اضافة الجلسة بنجاح", Toast.LENGTH_LONG).show()

                alertDialoge.dismiss()
            } else {
                Toast.makeText(
                    this,
                    "فضلا اكمل البيانات المطلوبة اولا! (رقم القضية، عام القضية وتاريخ الجلسة)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


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
                    (SimpleDateFormat("EEEE", Locale.getDefault()).format(myCalendar.time))
                var mydateformated = (SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(myCalendar.time))
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

                }


            },
            year,
            month,
            day
        )





        dpd.datePicker.firstDayOfWeek = Calendar.SUNDAY
        dpd.datePicker.minDate = (c.timeInMillis)
        c.add(Calendar.YEAR, 1)
        dpd.datePicker.maxDate = (c.timeInMillis)

        dpd.show()
        return
    }

}

