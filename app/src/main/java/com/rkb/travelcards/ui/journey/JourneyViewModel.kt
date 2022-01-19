package com.rkb.travelCards.ui.Plan

import androidx.lifecycle.*
import com.rkb.travelcards.Plan
import com.rkb.travelcards.TravelCardsRepository
import kotlinx.coroutines.launch

class JourneyViewModel(private val repository: TravelCardsRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPlans: LiveData<List<Plan>> = repository.allPlans.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(Plan: Plan) = viewModelScope.launch {
        repository.insert(Plan)
    }
}

class JourneyViewModelFactory(private val repository: TravelCardsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JourneyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}