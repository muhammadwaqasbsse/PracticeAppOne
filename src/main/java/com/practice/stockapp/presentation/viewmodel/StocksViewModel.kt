package com.practice.stockapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.stockapp.domain.model.Stock
import com.practice.stockapp.domain.repository.StockRepository
import com.practice.stockapp.domain.usecase.GetStocksUseCase
import com.practice.stockapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class StocksViewModel @Inject constructor(
    private val getStocksUseCase: GetStocksUseCase,
    private val repository: StockRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<Stock>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Stock>>> = _state.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    val searchResults = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapMerge { query ->
            if (query.isEmpty()) flowOf(emptyList())
            else repository.searchStocks(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    init {
        loadStocks()
    }

    fun loadStocks() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val stocks = repository.getStocks()
                _state.value = UiState.Success(stocks)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknow Error")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }
}
