package com.example.hca.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hca.data.repository.GithubRepository
import com.example.hca.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val repositories: List<Repository>, val isPagination: Boolean = false) :
        UiState()
    object PaginationLoading : UiState()
    data class Error(val message: String) : UiState()
}

@HiltViewModel
class GithubViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
    var currentPage = 1
    var isLastPage = false
    var isFetching = true
    var currentUsername = mutableStateOf("")

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchRepositories(username: String) {
        currentUsername.value = username
        currentPage = 1
        isLastPage = false
        isFetching = true

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val repositories =
                    repository.getRepositories(username = username, page = currentPage)
                _uiState.value = UiState.Success(repositories = repositories, isPagination = false)
                isFetching = false
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Unable to load data due to an ERROR..")
                isFetching = false
            }
        }
    }

    fun loadMoreRepositories(username: String) {
        if (isFetching || isLastPage) {
            return
        }

        isFetching = true
        currentPage++

        viewModelScope.launch {
            _uiState.value = UiState.PaginationLoading

            try {
                val newRepositories =
                    repository.getRepositories(username = username, page = currentPage)

                if (newRepositories.isEmpty()) {
                    isLastPage = true
                } else {
                    val currentRepositories =
                        (_uiState.value as? UiState.Success)?.repositories.orEmpty()
                    _uiState.value = UiState.Success(
                        repositories = currentRepositories + newRepositories, // Append new repositories
                        isPagination = false
                    )
                }

                isFetching = false
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error while loading more data.")
                isFetching = false
            }
        }
    }

    fun getRepositoryByName(repositoryName: String?): Repository? {
        val currentRepositories = (_uiState.value as? UiState.Success)?.repositories.orEmpty()
        return currentRepositories.find { it.name == repositoryName }
    }

}