package com.rkb.travelcards

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import androidx.appcompat.app.AppCompatDelegate


class TravelCardsApplication  : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // アプリ全体に適用

        super.onCreate()
    }

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy {
        CardRoomDatabase.getDatabase(this, applicationScope)
    }
    val repository by lazy {
        TravelCardsRepository(
            database.cardDao(),
            database.cardSuiteDao(),
            database.cardSuiteDao2(),
            database.planDao()
        )
    }
}