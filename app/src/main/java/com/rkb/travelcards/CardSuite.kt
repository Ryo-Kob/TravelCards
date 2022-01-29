package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "card_suite_table")
data class CardSuite(
    var isBlank: Boolean = true,
    var cardId: Int = 0,
    var startDate: Int = 0, // 日付をどうにかして数値にしたいが……
    var startTime: Int = 0, // 1分=1として数値化
    var isStartDateFixed: Boolean = false,
    var isStartTimeFixed: Boolean = false,
    var timer: Int = 0,
    var type: Int = VIEW_TYPE_CARD
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    var text: String = ""
    companion object {
        val VIEW_TYPE_EMPTY: Int = 0
        val VIEW_TYPE_CARD: Int = 1
    }
}