package com.rkb.travelcards

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSuiteDao {
    @Query("SELECT * FROM card_suite_table")
    fun getCardSuites(): Flow<List<CardSuite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardSuite: CardSuite)

    @Query("DELETE FROM card_suite_table")
    suspend fun deleteAll()
}