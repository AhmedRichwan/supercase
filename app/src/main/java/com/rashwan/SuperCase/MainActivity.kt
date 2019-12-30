package com.rashwan.SuperCase

//import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
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
import kotlinx.android.synthetic.main.edit_case.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onBackPressed() {
        var i = Intent()
        i.action = Intent.ACTION_MAIN
        i.addCategory(Intent.CATEGORY_HOME)
        this.startActivity(i)
    }

    var mRef: DatabaseReference? = null
    var mNotelist: ArrayList<Casesinfo>? = null

    var mAuth: FirebaseAuth? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var currentuser = mAuth?.currentUser?.email.toString()
        var currentusertrimed = currentuser.replace("@", "")
        currentusertrimed = currentusertrimed.replace(".", "")
        mRef = database.getReference("Cases+$currentusertrimed")
        mNotelist = ArrayList()
        addfltbtn.setOnClickListener {
            showdialogeAddnote()

        }

        new_list_view.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { p0, p1, p2, p3 ->
                var sortedList = mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()
                var casy = sortedList?.get(p2)!!

                val alertBuilder = AlertDialog.Builder(this)
                var update_case_view = layoutInflater.inflate(R.layout.edit_case, null)

//                var update_case_view = layoutInflater.inflate(R.layout.update_case, null)
                if (casy.deleted == 1) {
                    update_case_view.delbtn.text = "استرجاع"
                } else {
                    update_case_view.delbtn.text = "حذف"
                }


                val alertDialog = alertBuilder.create()

                alertDialog.setView(update_case_view)
                alertDialog.show()
                update_case_view.et1caseNumT.text =
                    " تعديل بيانات القضية رقم : " + casy.caseNum + " "
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
                        (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm"))) + " via SuperCaseApp",
                        0
                    )
                    childRef!!.setValue(afterUpdate)

                    Toast.makeText(this, "تم التعديل", Toast.LENGTH_LONG).show()

                    alertDialog.dismiss()
                }
                update_case_view.delbtn.setOnClickListener {
                    var childRef = mRef?.child(casy.Id!!)
                    var toasthint = "deletioncase"
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
                        (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm"))) + " via SuperCaseApp",
                        if (casy.deleted == 1) {
                            toasthint = "تم الاستعادة بنجاح"
                            0
                        } else {
                            toasthint = "تم الحذف"

                            1
                        }
                    )

                    childRef!!.setValue(afterUpdate)


                    Toast.makeText(this, toasthint, Toast.LENGTH_LONG).show()
                    //}
                    alertDialog.dismiss()


                }
                update_case_view.cancelbtn.setOnClickListener {
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
                case.Id
                var test = case.deleted.toString()

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
                caseIntent.putExtra("Id", case.Id)
                caseIntent.putExtra("deleted", case.deleted.toString())


                startActivity(caseIntent)


            }

    }


    var sortedList =
        mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()

//        val noteadapter = NotesAdapter(application, sortedList!!)


    fun LVA(view: View) {
        Pbar.isVisible=true
        TTitle.text = view.contentDescription
        val today = LocalDate.now()
        var firstdayofweek = today
        while (firstdayofweek.dayOfWeek !== DayOfWeek.SATURDAY) {
            firstdayofweek = firstdayofweek.minusDays(1)
        }

        var lastdayofweek = today
        while (lastdayofweek.dayOfWeek !== DayOfWeek.FRIDAY) {
            lastdayofweek = lastdayofweek.plusDays(1)
        }
        var firstdayofmonth = today
        while (firstdayofmonth.dayOfMonth != 1) {
            firstdayofmonth = firstdayofmonth.minusDays(1)
        }
        var    lastdayofmonth =firstdayofmonth.with(TemporalAdjusters.lastDayOfMonth())
var lastdayofnextmonth =firstdayofmonth.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth())

        var firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString())
        var lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())
        var delStat=false
        when {
            view.id == (R.id.allbtn) -> {
                firstDayOfWeek = "0".toLong()
                lastDayOfWeek = "999999999999".toLong()

            }
            view.id == (R.id.cwbtn) -> {

                firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString())
                lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())

            }
            view.id == (R.id.nwbtn) -> {
                firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString())+ 604800
                lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())+ 604800

            }
            view.id == (R.id.cm) -> {
                firstDayOfWeek = tools.strToEpoch(firstdayofmonth.toString())
                lastDayOfWeek = tools.strToEpoch(lastdayofmonth.toString())

            }
            view.id == (R.id.nm) -> {
                firstDayOfWeek = tools.strToEpoch(lastdayofmonth.toString())+86400
                lastDayOfWeek = tools.strToEpoch(lastdayofnextmonth.toString())

            }
            view.id == R.id.delbtn -> {
                firstDayOfWeek = "0".toLong()
                lastDayOfWeek = "999999999999".toLong()
                delStat=true

            }


        }

        mRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                mNotelist?.clear()
try {
    for (n in p0.children) {
        val case = n.getValue(Casesinfo::class.java)
        var checkDeleted = n.child("deleted").value
        var caseSessionDate = n.child("caseSessionDate").value.toString().toLong()
        if (delStat == false) {
            if (checkDeleted.toString() == "0" && (caseSessionDate >= firstDayOfWeek) && (caseSessionDate <= lastDayOfWeek)) {
                mNotelist!!.add(0, case!!)
            }
        } else {
            if (checkDeleted.toString() == "1" && (caseSessionDate >= firstDayOfWeek) && (caseSessionDate <= lastDayOfWeek)) {
                mNotelist!!.add(0, case!!)


            }

        }
//
        var sortedList =
            mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()

        val noteadapter = CasesAdapter(application, sortedList!!)
        Pbar.isVisible=false
        new_list_view.adapter = noteadapter
    }

}catch  (e : Exception){
Toast.makeText(this@MainActivity,e.message.toString(),Toast.LENGTH_LONG).show()
    Pbar.isVisible = false
}
                if (mNotelist?.count() == 0) {
                    Pbar.isVisible = false
                    TTitle.text = " عفوا لا يوجد قضايا في " + view.contentDescription
                } else {
                    val casecount = " ( " + mNotelist?.count().toString() + " )"
                    TTitle.text = "" + view.contentDescription + casecount

                }
            }
        })
    }



    fun showdialogeAddnote() {
        val alertbuilder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.edit_case, null)
        view.delbtn.isVisible = false
        view.updbtn.text = "اضافة"
        view.et1caseNumT.text = "اضافة قضية جديدة"


        alertbuilder.setView(view)
        val alertDialoge = alertbuilder.create()
        alertDialoge.show()




        view.et7caseSessionDate.setOnClickListener {
            pickDateValidation(view.et7caseSessionDate)

        }


        view.updbtn.setOnClickListener {

            if (view.et1caseNum.text.toString().isNotEmpty() && view.et2caseYear.text.toString().isNotEmpty() && view.et7caseSessionDate.text.toString().isNotEmpty()) {

                mAuth = FirebaseAuth.getInstance()
                val id = mRef!!.push().key!!
                val myCase = Casesinfo(
                    id,
                    view.et1caseNum.text.toString(),
                    view.et2caseYear.text.toString(),
                    view.et3caseCount.text.toString(),
                    view.et4caseCountYear.text.toString(),
                    view.et5caseAccuser.text.toString(),
                    view.et6caseCreater.text.toString() + mAuth?.currentUser?.email.toString()
                    ,
                    tools.strToEpoch(view.et7caseSessionDate.text.toString()).toString(),
                    view.et8casePapers.text.toString(),
                    view.et9caseNotes.text.toString(),
                    view.et10caseModifiedDate.text.toString() + (LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm")
                    )) + " via SuperCaseApp",
                    0
                )
                mRef!!.child(id).setValue(myCase)
                var sortedList = mNotelist?.sortedWith(compareBy({ it.caseSessionDate }))?.toList()
                val noteadapter = CasesAdapter(application, sortedList!!)
                new_list_view.adapter = noteadapter

                Toast.makeText(this, "تم اضافة الجلسة بنجاح", Toast.LENGTH_LONG).show()
                LVA(allbtn)


                alertDialoge.dismiss()
            } else {
                Toast.makeText(
                    this,
                    "فضلا اكمل البيانات المطلوبة اولا! (رقم القضية، عام القضية وتاريخ الجلسة)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        view.cancelbtn.setOnClickListener {
            alertDialoge.hide()

        }


    }

    fun pickDateValidation(textview: TextView): Long {
        var orginalvalue = textview.text
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
        textview.text = orginalvalue

        return result
    }

    fun save() {
        var gson = Gson()
        val filename = "name"
        val file = application?.getFileStreamPath(filename)
        if (file == null || !file.exists()) {
            val array = ArrayList<Casesinfo>()

            var json: String = gson.toJson(array).replace("\\n", "\n")
            application?.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it?.write(json.toByteArray())
            }
        } else {
            val file1 = File(application?.filesDir, filename)
            val contents = file1.readText()
            val array = gson.fromJson(contents, Array<Casesinfo>::class.java)
            val arrayList = ArrayList(array.toMutableList())
            val json: String = gson.toJson(arrayList).replace("\\n", "\n")
            application?.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it?.write(json.toByteArray())
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id=item.itemId

        when {
            (id==R.id.logout)-> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, Welcome::class.java))
            }
            (id == R.id.cw) -> LVA(cwbtn)
            (id == R.id.nw) -> LVA(nwbtn)
            (id == R.id.cm) -> LVA(cm)
            (id == R.id.nm) -> LVA(nm)
            (id == R.id.all) -> LVA(allbtn )
            (id == R.id.del) -> LVA(delbtn)
            (id == R.id.exit) -> finishAffinity()
        }
        return true
    }

}
