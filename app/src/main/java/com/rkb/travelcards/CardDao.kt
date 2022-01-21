package com.rkb.travelcards

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM card_table")
    fun getCards(): Flow<List<Card>>

    @Query("SELECT * FROM card_table")
    fun getCardList(): List<Card>

    @Query("SELECT * FROM card_table WHERE id=:id")
    fun getCard(id: Int): Card

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(card: Card)

    @Query("DELETE FROM card_table")
    suspend fun deleteAll()
}