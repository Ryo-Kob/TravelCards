package com.rkb.travelcards.ui.planEditor1

import androidx.lifecycle.*
import com.rkb.travelcards.Card
import com.rkb.travelcards.TravelCardsRepository
import kotlinx.coroutines.launch

class DrawerPlanViewModel(private val repository: TravelCardsRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCards: LiveData<List<Card>> = repository.allCards.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(card: Card) = viewModelScope.launch {
        repository.insert(card)
    }
}

class DrawerPlanViewModelFactory(private val repository: TravelCardsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrawerPlanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrawerPlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}