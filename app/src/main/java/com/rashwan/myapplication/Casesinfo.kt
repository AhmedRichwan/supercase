
package com.rashwan.myapplication
import com.google.firebase.database.ServerValue
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

import java.util.*

 class Casesinfo {
     var Id: String? = null
     var caseNum: String? = null
     var caseYear: String? = null
     var caseCount: String? = null
     var caseCountYear: String? = null
     var caseAccuser: String? = null
     var caseCreater: String? = null
     var caseSessionDate: String? = null
     var casePapers: String? = null
     var caseNotes: String? = null
     var caseModifiedDate: String? = null
     var IsCaseDeleted: Int? = null


     constructor() {}


     constructor(
         Id: String, caseNum: String, caseYear: String, caseCount: String, caseCountYear: String,
         caseAccuser: String, caseCreater: String, caseSessionDate: String,
         casePapers: String, caseNotes: String, caseModifiedDate: String, IsCaseDeleted: Int
     ) {
         this.Id = Id
         this.caseNum = caseNum
         this.caseYear = caseYear
         this.caseCount = caseCount
         this.caseCountYear = caseCountYear
         this.caseAccuser = caseAccuser
         this.caseCreater = caseCreater
         this.caseSessionDate = caseSessionDate
         this.casePapers = casePapers
         this.caseNotes = caseNotes
         this.caseModifiedDate = caseModifiedDate
         this.IsCaseDeleted = IsCaseDeleted

     }

 }

