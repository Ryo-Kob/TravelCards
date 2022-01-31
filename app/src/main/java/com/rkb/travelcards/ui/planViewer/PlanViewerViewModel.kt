package com.rkb.travelcards.ui.planViewer

import androidx.lifecycle.*
import com.rkb.travelcards.Card
import com.rkb.travelcards.CardSuite
import com.rkb.travelcards.CardSuite2
import com.rkb.travelcards.TravelCardsRepository
import kotlinx.coroutines.launch


class PlanViewerViewModel(private val repository: TravelCardsRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCardSuites: LiveData<List<CardSuite>> = repository.allCardSuites.asLiveData()
    val allCardSuites2: LiveData<List<CardSuite2>> = repository.allCardSuites2.asLiveData()
//    val allCards: LiveData<List<Card>> = repository.allCards.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cardSuite: CardSuite) = viewModelScope.launch {
        repository.insert(cardSuite)
    }
    fun insert2(cardSuite2: CardSuite2) = viewModelScope.launch {
        repository.insert2(cardSuite2)
    }

    fun delete(id : Int) = viewModelScope.launch {
        repository.delete(id)
    }
    fun delete2(id : Int) = viewModelScope.launch {
        repository.delete2(id)
    }

    fun getCard(cardId: Int) = viewModelScope.launch {
        repository.getCard(cardId)
    }

    fun getCardList() : List<Card> {
        return repository.getCardList()
    }

    fun getCardSuiteList() : List<CardSuite> {
        return repository.getCardSuiteList()
    }
    fun getCardSuiteList2() : List<CardSuite2> {
        return repository.getCardSuiteList2()
    }

    fun deleteAllCardSuites() = viewModelScope.launch {
        repository.deleteAllCardSuites()
    }
}

class PlanViewerViewModelFactory(private val repository: TravelCardsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanViewerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanViewerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}