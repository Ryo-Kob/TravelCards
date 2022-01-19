package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "card_suite_table")
data class CardSuite(
    var cardId: Int = 0,
    var startDate: Int = 0,
    var startTime: Int = 0,
    var isStartDateFixed: Boolean = false,
    var isStartTimeFixed: Boolean = false,
    var timer: Int = 0
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}