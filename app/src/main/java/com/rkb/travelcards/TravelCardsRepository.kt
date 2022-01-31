package com.rkb.travelcards

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TravelCardsRepository(
    private val cardDao: CardDao,
    private val cardSuiteDao: CardSuiteDao,
    private val cardSuiteDao2: CardSuiteDao2,
    private val planDao: PlanDao
    ) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allCards: Flow<List<Card>> = cardDao.getCards()
    val allCardSuites: Flow<List<CardSuite>> = cardSuiteDao.getCardSuites()
    val allCardSuites2: Flow<List<CardSuite2>> = cardSuiteDao2.getCardSuites()
    val allPlans: Flow<List<Plan>> = planDao.getPlans()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(card: Card) {
        cardDao.insert(card)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(cardSuite: CardSuite) {
        cardSuiteDao.insert(cardSuite)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(id: Int) {
        cardSuiteDao.delete(id)
    }

    suspend fun updateStartTime(startTime: Int, id: Int) {
        cardSuiteDao.updateStartTime(startTime, id)
    }

    suspend fun deleteAllCardSuites() {
        cardSuiteDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert2(cardSuite: CardSuite2) {
        cardSuiteDao2.insert(cardSuite)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete2(id: Int) {
        cardSuiteDao2.delete(id)
    }

    suspend fun updateStartTime2(startTime: Int, id: Int) {
        cardSuiteDao2.updateStartTime(startTime, id)
    }

    suspend fun deleteAllCardSuites2() {
        cardSuiteDao2.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(plan: Plan) {
        planDao.insert(plan)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCard(id: Int) {
        cardDao.getCard(id)
    }


    fun getCardList() : List<Card> {
        return cardDao.getCardList()
    }

    fun getCardSuiteList() : List<CardSuite> {
        return cardSuiteDao.getCardSuiteList()
    }

}