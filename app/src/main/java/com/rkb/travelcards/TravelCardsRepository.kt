package com.rkb.travelcards

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TravelCardsRepository(
    private val cardDao: CardDao,
    private val cardSuiteDao: CardSuiteDao,
    private val planDao: PlanDao
    ) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allCards: Flow<List<Card>> = cardDao.getCards()
    val allCardSuites: Flow<List<CardSuite>> = cardSuiteDao.getCardSuites()
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
}