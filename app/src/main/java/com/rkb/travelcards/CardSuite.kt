package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "card_suite_table")
data class CardSuite(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var cardId: Int,
    var startDate: Int = 0,
    var startTime: Int = 0,
    var isStartDateFixed: Boolean = false,
    var isStartTimeFixed: Boolean = false,
    var timer: Int = 0
)