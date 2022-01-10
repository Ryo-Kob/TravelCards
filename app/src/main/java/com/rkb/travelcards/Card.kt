package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "card_table")
data class Card(
        @PrimaryKey(autoGenerate = true) val id: Int,
        var title : String = "",
        var description : String = "",
//        var startTime : Calendar,
//        var timerHour : Int,
//        var timerMinute : Int
)