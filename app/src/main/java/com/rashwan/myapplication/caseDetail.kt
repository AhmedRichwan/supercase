package com.rashwan.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.case_detail.*

class caseDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.case_detail)


        caseNum.text="رقم القضية"
        caseYear.text="عام القضية"
        caseCount.text="رقم الحصر"
        caseCountYear.text="عام الحصر"
        caseCreater.text="المدعي: "+intent.extras!!.getString("caseCreater")
        caseAccuser.text="المتهم : "+intent.extras!!.getString("caseAccuser")
        casePapers.text="اوراق ومستندات القضية: "+intent.extras!!.getString("casePapers")
        caseNotes.text="ملاحظات القضية: "+intent.extras!!.getString("caseNotes")
        caseSessionDate.text="تاريخ الجلسة: "+tools.epochToStr( intent.extras!!.getString("caseSessionDate")!!.toLong())
        caseModifiedDate.text=" تسجيل البيانات: "+intent.extras!!.getString("caseModifiedDate")
        caseNumV.text=intent.extras!!.getString("caseNum")
        caseYearV.text=intent.extras!!.getString("caseYear")
        caseCountV.text=intent.extras!!.getString("caseCount")
        caseCountYearV.text=intent.extras!!.getString("caseCountYear")


    }
}
