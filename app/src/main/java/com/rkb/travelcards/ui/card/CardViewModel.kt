package com.rkb.travelcards.ui.card

import androidx.lifecycle.*
import com.rkb.travelcards.Card
import com.rkb.travelcards.TravelCardRepository
import kotlinx.coroutines.launch

class CardViewModel(private val repository: TravelCardRepository) : ViewModel() {

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

class CardViewModelFactory(private val repository: TravelCardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}