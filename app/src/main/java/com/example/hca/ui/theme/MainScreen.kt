package com.example.hca.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hca.viewmodel.GithubViewModel
import com.example.hca.viewmodel.UiState
import com.example.hca.data.model.Repository

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            label = { Text("Search for a user or organization") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (searchText.isNotBlank()) {
                    onSearch()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
    }
}

@Composable
fun MainScreen(
    viewModel: GithubViewModel = hiltViewModel(), // ViewModel to manage the state
    onRepositoryClick: (Repository) -> Unit // Handle repository item click
) {
    val uiState by viewModel.uiState.collectAsState() // Collect UI state from ViewModel
    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == lazyListState.layoutInfo.totalItemsCount - 1 }
            .collect { isAtBottom ->
                if (isAtBottom) {
                    viewModel.loadMoreRepositories(viewModel.currentUsername.value)
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            searchText = viewModel.currentUsername.value, // Current search text
            onSearchTextChanged = { viewModel.currentUsername.value = it }, // Update search text
            onSearch = { viewModel.fetchRepositories(viewModel.currentUsername.value) } // Trigger search in ViewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is UiState.Idle -> Text("Enter a username or organization to search")

            is UiState.Loading -> CircularProgressIndicator()

            is UiState.Success -> {
                val repositories = (uiState as UiState.Success).repositories
                LazyColumn(state = lazyListState) {
                    items(repositories) { repo ->
                        RepositoryItem(repo, onClick = { onRepositoryClick(repo) })
                    }
                    item {
                        if ((uiState as UiState.Success).isPagination) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }

            is UiState.PaginationLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator() // Show spinner when loading more items
                }
            }

            is UiState.Error -> Text("Error: ${(uiState as UiState.Error).message}")
        }
    }
}

@Composable
fun RepositoryItem(
    repository: Repository,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = repository.name ?: "Unknown Repository",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = repository.description ?: "No description available",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Language: ${repository.language ?: "Unknown"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Stars: ${repository.stargazersCount}, Forks: ${repository.forksCount}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun RepositoryDetailScreen(repository: Repository) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Repository: ${repository.name}", style = MaterialTheme.typography.titleSmall)
        Text(
            text = "Description: ${repository.description ?: "No description available"}",
            style = MaterialTheme.typography.titleSmall
        )
        Text(text = "Owner: ${repository.owner.login}", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Stars: ${repository.stargazersCount}, Forks: ${repository.forksCount}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}
