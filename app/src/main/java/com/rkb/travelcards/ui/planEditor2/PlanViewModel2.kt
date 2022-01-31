package com.rkb.travelcards.ui.planEditor2

import androidx.lifecycle.*
import com.rkb.travelcards.Card
import com.rkb.travelcards.CardSuite2
import com.rkb.travelcards.TravelCardsRepository
import kotlinx.coroutines.launch

class PlanViewModel2(private val repository: TravelCardsRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCardSuites: LiveData<List<CardSuite2>> = repository.allCardSuites2.asLiveData()
//    val allCards: LiveData<List<Card>> = repository.allCards.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cardSuite: CardSuite2) = viewModelScope.launch {
        repository.insert2(cardSuite)
    }

    fun delete(id : Int) = viewModelScope.launch {
        repository.delete2(id)
    }

    fun updateStartTime(startTime: Int, id: Int) = viewModelScope.launch {
        repository.updateStartTime2(startTime, id)
    }

    fun getCard(cardId: Int) = viewModelScope.launch {
        repository.getCard(cardId)
    }

    fun getCardList() : List<Card> {
        return repository.getCardList()
    }

    fun getCardSuiteList() : List<CardSuite2> {
        return repository.getCardSuiteList2()
    }

    fun deleteAllCardSuites() = viewModelScope.launch {
        repository.deleteAllCardSuites2()
    }
}

class PlanViewModelFactory2(private val repository: TravelCardsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanViewModel2::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanViewModel2(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}