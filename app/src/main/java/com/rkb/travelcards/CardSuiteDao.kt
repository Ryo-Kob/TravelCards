package com.rkb.travelcards

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSuiteDao {
    @Query("SELECT * FROM card_suite_table ORDER BY startTime")
    fun getCardSuites(): Flow<List<CardSuite>>

    @Query("SELECT * FROM card_suite_table")
    fun getCardSuiteList(): List<CardSuite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardSuite: CardSuite)

    @Query("DELETE FROM card_suite_table WHERE id=:id")
    fun delete(id : Int)

    @Query("UPDATE card_suite_table SET startTime=:startTime WHERE id=:id")
    fun updateStartTime(startTime: Int, id: Int)

    @Query("DELETE FROM card_suite_table")
    suspend fun deleteAll()
}