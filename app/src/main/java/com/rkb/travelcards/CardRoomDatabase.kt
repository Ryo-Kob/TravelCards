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
    CardSuite2::class,
    Plan::class), version = 1, exportSchema = false)
public abstract class CardRoomDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun cardSuiteDao() : CardSuiteDao
    abstract fun cardSuiteDao2() : CardSuiteDao2
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
                    populateDatabase(database.cardSuiteDao2())
                    populateDatabase(database.planDao())
                }
            }
        }

        suspend fun populateDatabase(cardDao: CardDao) {
            // Delete all content here.
            cardDao.deleteAll()

            // Add sample words.
            var card = Card()
//            card.title = "(例) ××レストランでランチ"
//            card.description = ""
//            card.strStartDate = ""
//            card.strStartTime = ""
//            card.isStartDateSet = false
//            card.isStartTimeSet =false
//            card.strDateTime = "1 時間 0 分"
//            card.timerHour = 1
//            card.timerMinute = 0
//            cardDao.insert(card)

            // さらにサンプルワード。デモ用
            card.title = "東京 → 京都 新幹線 <予約済み>"
            card.description = ""
            card.strStartDate = ""
            card.strStartTime = "07:00"
            card.isStartDateSet = false
            card.isStartTimeSet = true
            card.strDateTime = "7:00 から 2 時間 30 分"
            card.timerHour = 2
            card.timerMinute = 30
            cardDao.insert(card)

            card.title = "京都 → 奈良 （JR）"
            card.description = ""
            card.strStartDate = ""
            card.strStartTime = ""
            card.isStartDateSet = false
            card.isStartTimeSet = false
            card.strDateTime = "1 時間 30 分"
            card.timerHour = 1
            card.timerMinute = 30
            cardDao.insert(card)

            card.title = "京都 → 奈良 （近鉄）"
            card.description = ""
            card.strStartDate = ""
            card.strStartTime = ""
            card.isStartDateSet = false
            card.isStartTimeSet = false
            card.strDateTime = "1 時間 0 分"
            card.timerHour = 1
            card.timerMinute = 0
            cardDao.insert(card)

//            card.title = "細かい移動時間"
//            card.description = ""
//            card.strStartDate = ""
//            card.strStartTime = ""
//            card.isStartDateSet = false
//            card.isStartTimeSet = false
//            card.strDateTime = "1 時間 0 分"
//            card.timerHour = 1
//            card.timerMinute = 0
//            cardDao.insert(card)

            card.title = "ランチ in 京都"
            card.description = ""
            card.strStartDate = ""
            card.strStartTime = ""
            card.isStartDateSet = false
            card.isStartTimeSet = false
            card.strDateTime = "0 時間 45 分"
            card.timerHour = 0
            card.timerMinute = 45
            cardDao.insert(card)

            card.title = "お寺 見学"
            card.description = ""
            card.strStartDate = ""
            card.strStartTime = ""
            card.isStartDateSet = false
            card.isStartTimeSet = false
            card.strDateTime = "2 時間 0 分"
            card.timerHour = 2
            card.timerMinute = 0
            cardDao.insert(card)

            card.title = "カフェで休憩"
            card.description = ""
            card.strStartDate = ""
            card.strStartTime = ""
            card.isStartDateSet = false
            card.isStartTimeSet = false
            card.strDateTime = "1 時間 30 分"
            card.timerHour = 1
            card.timerMinute = 30
            cardDao.insert(card)

            card.title = "ホテルチェックイン in 奈良xxホテル"
            card.description = ""
            card.strStartDate = ""
            card.strStartTime = "17:00"
            card.isStartDateSet = false
            card.isStartTimeSet = true
            card.strDateTime = "17:00 から 3 時間 0 分"
            card.timerHour = 3
            card.timerMinute = 0
            cardDao.insert(card)
        }

        suspend fun populateDatabase(cardSuiteDao: CardSuiteDao) {
            // Delete all content here.
            cardSuiteDao.deleteAll()

            // Add sample words.
//            var cardSuite = CardSuite()
//            cardSuiteDao.insert(cardSuite)
        }

        suspend fun populateDatabase(cardSuiteDao2: CardSuiteDao2) {
            // Delete all content here.
            cardSuiteDao2.deleteAll()

            // Add sample words.
//            var cardSuite = CardSuite()
//            cardSuiteDao.insert(cardSuite)
        }

        suspend fun populateDatabase(planDao: PlanDao) {
            // Delete all content here.
            planDao.deleteAll()

            // Add sample words.
//            var plan = Plan()
//            planDao.insert(plan)
        }
    }
}