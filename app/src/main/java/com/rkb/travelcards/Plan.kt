package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "plan_table")
data class Plan(
    var title : String = "",
    var description : String = "",
//    var cardSuite: CardSuite
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}