package com.rkb.travelcards

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Card::class), version = 1, exportSchema = false)
public abstract class CardRoomDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CardRoomDatabase? = null

        fun getDatabase(context: Context): CardRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardRoomDatabase::class.java,
                    "card_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}