package com.rkb.travelCards.ui.Plan

import androidx.lifecycle.*
import com.rkb.travelcards.CardSuite
import com.rkb.travelcards.TravelCardsRepository
import kotlinx.coroutines.launch

class PlanViewModel(private val repository: TravelCardsRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCardSuites: LiveData<List<CardSuite>> = repository.allCardSuites.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cardSuite: CardSuite) = viewModelScope.launch {
        repository.insert(cardSuite)
    }
}

class PlanViewModelFactory(private val repository: TravelCardsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}