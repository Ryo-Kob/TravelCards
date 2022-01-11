package com.rkb.travelcards

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    @Query("SELECT * FROM plan_table")
    fun getPlans(): Flow<List<Plan>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(Plan: Plan)

    @Query("DELETE FROM plan_table")
    suspend fun deleteAll()
}