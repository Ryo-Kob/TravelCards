package com.rkb.travelcards

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSuiteDao2 {
    @Query("SELECT * FROM card_suite_table2 ORDER BY startTime")
    fun getCardSuites(): Flow<List<CardSuite2>>

    @Query("SELECT * FROM card_suite_table2")
    fun getCardSuiteList(): List<CardSuite2>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardSuite: CardSuite2)

    @Query("DELETE FROM card_suite_table2 WHERE id=:id")
    fun delete(id : Int)

    @Query("UPDATE card_suite_table2 SET startTime=:startTime WHERE id=:id")
    fun updateStartTime(startTime: Int, id: Int)

    @Query("DELETE FROM card_suite_table2")
    suspend fun deleteAll()
}