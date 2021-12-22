package com.rkb.travelcards

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM card_table ORDER BY id ASC")
    fun getCards(): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(card: Card)

    @Query("DELETE FROM card_table")
    fun deleteAll()
}