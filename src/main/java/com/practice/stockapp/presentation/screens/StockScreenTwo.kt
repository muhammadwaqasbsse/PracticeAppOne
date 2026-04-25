package com.practice.stockapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.dreammobileapps.practiceappone.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practice.stockapp.domain.model.Stock
import com.practice.stockapp.presentation.viewmodel.StocksViewModel
import com.practice.stockapp.util.UiState

@Preview(showBackground = true)
@Composable
fun StockScreenTwo(viewModel: StocksViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    var query by remember { mutableStateOf("") }
    val isSearching = query.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.onSearchQueryChanged(it)
            },
            label = { Text(stringResource(R.string.search_stocks)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isSearching) {
            LazyColumn {
                items(searchResults, key = {it.id }) { stock ->
                    StockItem(stock)
                }
            }
            return@Column
        }

        when(val s = state) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                LazyColumn {
                    items(s.data, key = {it.id}) { stock ->
                        StockItem(stock)
                    }
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = s.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadStocks() }) {
                            Text(stringResource(R.string.retry ))
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun StockItemTwo(stock: Stock) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${stock.price}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stock.changePercent}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (stock.changePercent >= 0)
                        Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}