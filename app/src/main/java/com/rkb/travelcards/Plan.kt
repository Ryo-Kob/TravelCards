package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "plan_table")
data class Plan(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title : String = "",
    var description : String = "",
    var cardSuite: CardSuite
)