package com.rkb.travelcards

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_table")
data class Card(
        @PrimaryKey(autoGenerate = true) val id: Int,
        var title : String = "",
        var description : String = ""
)