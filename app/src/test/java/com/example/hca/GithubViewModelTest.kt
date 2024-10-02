package com.example.hca

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hca.data.model.Owner
import com.example.hca.data.model.Repository
import com.example.hca.data.repository.GithubRepository
import com.example.hca.viewmodel.GithubViewModel
import com.example.hca.viewmodel.UiState
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class GithubViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var githubRepository: GithubRepository
    private lateinit var githubViewModel: GithubViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        githubRepository = mock(GithubRepository::class.java)

        githubViewModel = GithubViewModel(githubRepository)
    }

    @Test
    fun `fetchRepositories should emit Success state when data is fetched successfully`() = runTest {
        val username = "testUser"
        val repositories = listOf(
            Repository(1, "Repo1", "Description1", "kotlin", 12, mock(), owner = Owner(id = 101, login = "testOwner1")),
            Repository(2, "Repo2", "Description2", "Java", 12, mock(), owner = Owner(id = 102, login = "testOwner2"))
        )

        `when`(githubRepository.getRepositories(username, 1)).thenReturn(repositories)

        githubViewModel.fetchRepositories(username)

        val uiState = githubViewModel.uiState.first()
        assertTrue(uiState is UiState.Success)
        assertEquals(repositories, (uiState as UiState.Success).repositories)
    }

    @Test
    fun `fetchRepositories should emit Error state when an exception occurs`() = runTest {
        val username = "testUser"

        `when`(githubRepository.getRepositories(username, 1)).thenThrow(RuntimeException("Error"))

        githubViewModel.fetchRepositories(username)

        val uiState = githubViewModel.uiState.first()
        assertTrue(uiState is UiState.Error)
        assertEquals("Unable to load data due to an ERROR..", (uiState as UiState.Error).message)
    }

    @Test
    fun `loadMoreRepositories should emit Success state when new data is fetched successfully`(): Unit = runTest {
        val username = "testUser"
        val initialRepositories = listOf(
            Repository(1, "Repo1", "Description1", "kotlin", 12, mock(), owner = Owner(id = 101, login = "testOwner1"))
        )
        val newRepositories = listOf(
            Repository(2, "Repo2", "Description2", "Java", 12, mock(), owner = Owner(id = 102, login = "testOwner2"))
        )

        `when`(githubRepository.getRepositories(username, 1)).thenReturn(initialRepositories)
        `when`(githubRepository.getRepositories(username, 2)).thenReturn(newRepositories)

        githubViewModel.fetchRepositories(username)

        githubViewModel.loadMoreRepositories(username)

        val uiState = githubViewModel.uiState.first()
        assertTrue(uiState is UiState.Success)
        assertEquals(2, (uiState as UiState.Success).repositories.size)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}