package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "card_table")
data class Card(
        var title : String = "",
        var description : String = "",
        var strStartDate : String = "",
        var strStartTime : String = "",
        var isStartDateSet : Boolean = false,
        var isStartTimeSet : Boolean = false,
        var strStartDateTime : String = "(スケジュール未設定)",
        var timerHour : Int = 0,
        var timerMinute : Int = 0,
) {
        @PrimaryKey(autoGenerate = true) var id: Int = 0
}