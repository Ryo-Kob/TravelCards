package com.rkb.travelcards

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(
    Card::class,
    CardSuite::class,
    Plan::class), version = 1, exportSchema = false)
public abstract class CardRoomDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun cardSuiteDao() : CardSuiteDao
    abstract fun planDao() : PlanDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CardRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CardRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardRoomDatabase::class.java,
                    "card_database"
                ).addCallback(CardDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class CardDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.cardDao())
                    populateDatabase(database.cardSuiteDao())
                    populateDatabase(database.planDao())
                }
            }
        }

        suspend fun populateDatabase(cardDao: CardDao) {
            // Delete all content here.
            cardDao.deleteAll()

            // Add sample words.
            var card = Card()
            card.title = "(サンプル) ××レストランでランチ"
            card.strDateTime = "1/1 12:00 - 14:00"
            cardDao.insert(card)
        }

        suspend fun populateDatabase(cardSuiteDao: CardSuiteDao) {
            // Delete all content here.
            cardSuiteDao.deleteAll()

            // Add sample words.
            var cardSuite = CardSuite()
            cardSuiteDao.insert(cardSuite)

            // TODO: Add your own words!
        }

        suspend fun populateDatabase(planDao: PlanDao) {
            // Delete all content here.
            planDao.deleteAll()

            // Add sample words.
            var plan = Plan()
            planDao.insert(plan)

            // TODO: Add your own words!
        }
    }
}