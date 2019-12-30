package com.rashwan.SuperCase

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class tools {

    companion object MyTools {



        fun epochToStr(e: Long): String {

            return LocalDateTime.ofInstant(Instant.ofEpochSecond(e), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE"))
        }


        fun epochToStrWithoutEEE(e: Long): String {

            return LocalDateTime.ofInstant(Instant.ofEpochSecond(e), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        fun strToEpoch( l: String): Long {
         //   var edited=l.replace(" Sun","")
            var edited=l
            if (l.length!=10){
             edited= l.substring(0, 10)}

            else  edited=l

                  return LocalDateTime.parse(edited+"T00:00:00.000").atZone(ZoneOffset.systemDefault()).toEpochSecond()
        }

        fun getDaysDif(fromDate: LocalDate, toDate: LocalDate): Long {
            return ChronoUnit.DAYS.between(fromDate, toDate)
        }

        fun getLocalDateFromString(d: String, format: String): LocalDate {
            return LocalDate.parse(d, DateTimeFormatter.ofPattern(format))
        }
//         fun dateConverter(sentVal: Any): Any {
//
//             if (sentVal is String) {
//                 return LocalDateTime.parse(sentVal, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                     .atZone(ZoneOffset.systemDefault()).toEpochSecond()
//             }
//             else if (sentVal is Long) {
//                 return LocalDateTime.ofInstant(Instant.ofEpochSecond(sentVal), ZoneId.systemDefault())
//                     .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//             }
//                 return sentVal
//         }
    }
}



