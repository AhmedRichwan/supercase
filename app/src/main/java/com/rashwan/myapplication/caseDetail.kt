package com.rashwan.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.case_detail.*
import kotlinx.android.synthetic.main.edit_case.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class caseDetail : AppCompatActivity() {
    var mRef: DatabaseReference? = null
    var mNotelist: ArrayList<Casesinfo>? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.case_detail)
        mRef = database.getReference("Cases")
        caseNum.text = "رقم القضية"
        caseYear.text = "عام القضية"
        caseCount.text = "رقم الحصر"
        caseCountYear.text = "عام الحصر"
        caseCreater.text = intent.extras!!.getString("caseCreater")
        caseAccuser.text = intent.extras!!.getString("caseAccuser")
        casePapers.text = intent.extras!!.getString("casePapers")
        caseNotes.text = intent.extras!!.getString("isCaseDeleted")
        caseSessionDate.text =
            tools.epochToStr(intent.extras!!.getString("caseSessionDate")!!.toLong())
        caseModifiedDate.text = intent.extras!!.getString("caseModifiedDate")
        caseNumV.text = intent.extras!!.getString("caseNum")
        et1caseNumT2.text = "التفاصيل الكاملة للقضية رقم  " + intent.extras!!.getString("caseNum")
        caseYearV.text = intent.extras!!.getString("caseYear")
        caseCountV.text = intent.extras!!.getString("caseCount")
        caseCountYearV.text = intent.extras!!.getString("caseCountYear")
        var idval = intent.extras!!.getString("Id")
        var deletion = intent.extras!!.getString("isCaseDeleted")

        inneredit.setOnClickListener {
            var sortedList = mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()


            val alertBuilder = AlertDialog.Builder(this)
            var update_case_view = layoutInflater.inflate(R.layout.edit_case, null)

            if (deletion?.toInt() == 1) {
                et1caseNumT2.text =
                    "تفاصيل القضية المحذوفة   رقم  " + update_case_view.et1caseNum.text.toString()
                et1caseNumT2.setBackgroundResource(R.drawable.background_delete)
                update_case_view.delbtn.text = "استرجاع"
            } else {
                update_case_view.delbtn.text = "حذف"
            }


            val alertDialog = alertBuilder.create()

            alertDialog.setView(update_case_view)
            alertDialog.show()

            update_case_view.et1caseNumT.text = " تعديل بيانات القضية رقم :   " + caseNumV.text
            update_case_view.et1caseNum.setText(caseNumV.text)
            update_case_view.et2caseYear.setText(caseYearV.text)
            update_case_view.et3caseCount.setText(caseCountV.text)
            update_case_view.et4caseCountYear.setText(caseCountYearV.text)
            update_case_view.et5caseAccuser.setText(caseAccuser.text)
            update_case_view.et6caseCreater.setText(caseCreater.text)
            update_case_view.et7caseSessionDate.text = (caseSessionDate.text)
            update_case_view.et8casePapers.setText(casePapers.text)
            update_case_view.et9caseNotes.setText(caseNotes.text)
            update_case_view.et10caseModifiedDate.setText(caseModifiedDate.text)
            update_case_view.et7caseSessionDate.setOnClickListener {
                pickDateValidation(update_case_view.et7caseSessionDate)
            }


            update_case_view.updbtn.setOnClickListener {

                var childRef = mRef?.child(idval!!)

                var afterUpdate = Casesinfo(
                    intent.extras!!.getString("Id")!!,
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

                Toast.makeText(this, "تم التعديل", Toast.LENGTH_LONG).show()

                caseNum.text = update_case_view.et1caseNum.text.toString()
                caseYear.text = update_case_view.et2caseYear.text.toString()
                caseCount.text = update_case_view.et3caseCount.text.toString()
                caseCountYear.text = update_case_view.et4caseCountYear.text.toString()
                caseAccuser.text = update_case_view.et5caseAccuser.text.toString()
                caseCreater.text = update_case_view.et6caseCreater.text.toString()
                caseSessionDate.text = update_case_view.et7caseSessionDate.text.toString()
                casePapers.text = update_case_view.et8casePapers.text.toString()
                caseNotes.text = update_case_view.et9caseNotes.text.toString()
                caseModifiedDate.text = update_case_view.et10caseModifiedDate.text.toString()
                et1caseNumT2.text =
                    "التفاصيل الكاملة للقضية رقم  " + update_case_view.et1caseNum.text.toString()
                et1caseNumT2.setBackgroundResource(R.color.myblackLight)




                alertDialog.dismiss()
            }
            update_case_view.delbtn.setOnClickListener {

                var childRef = mRef?.child(intent.extras!!.getString("Id")!!)

                var afterUpdate = Casesinfo(
                    intent.extras!!.getString("Id")!!,
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
                    if (deletion?.toInt() == 1) {
                        0
                    } else {
                        1
                    }
                )
                childRef!!.setValue(afterUpdate)
                //  if (view2.ite) {
                Toast.makeText(this, "تم الحذف", Toast.LENGTH_LONG).show()

                caseNum.text = update_case_view.et1caseNum.text.toString()
                caseYear.text = update_case_view.et2caseYear.text.toString()
                caseCount.text = update_case_view.et3caseCount.text.toString()
                caseCountYear.text = update_case_view.et4caseCountYear.text.toString()
                caseAccuser.text = update_case_view.et5caseAccuser.text.toString()
                caseCreater.text = update_case_view.et6caseCreater.text.toString()
                caseSessionDate.text = update_case_view.et7caseSessionDate.text.toString()
                casePapers.text = update_case_view.et8casePapers.text.toString()
                caseNotes.text = update_case_view.et9caseNotes.text.toString()
                caseModifiedDate.text = update_case_view.et10caseModifiedDate.text.toString()
                et1caseNumT2.text =
                    "تفاصيل القضية المحذوفة   رقم  " + update_case_view.et1caseNum.text.toString()
                et1caseNumT2.setBackgroundResource(R.drawable.background_delete)

                alertDialog.dismiss()


            }
            update_case_view.cancelbtn.setOnClickListener {
                alertDialog.dismiss()
            }
            true
        }

    }


    fun pickDateValidation(textview: TextView): Long {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        var pure = year.toString() + month.toString() + day.toString()
        var result: Long = 0

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, _, _, dayOfMonth ->
            val myCalendar = GregorianCalendar(year, month, dayOfMonth)
            var dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK)
            var mydateformated =
                (SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.time))

            var timecaptured = myCalendar.time

            if (dayOfWeek > 5) {

                Toast.makeText(
                    this,
                    "لا يمكن اختيار يوم عطلة ، يرجى التأكد من التاريخ والمحاولة ثانية!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                //  textview.text = mydateformated

                var a = myCalendar.timeInMillis.toString()
                var b = a.substring(0, 10).toLong()
                result = b
                // day=result
                textview.text = tools.epochToStr(b)


            }


        }, year, month, day)


        dpd.datePicker.firstDayOfWeek = Calendar.SUNDAY
        dpd.datePicker.minDate = (c.timeInMillis)
        dpd.show()
        textview.text = result.toString()
        return result
    }

}
