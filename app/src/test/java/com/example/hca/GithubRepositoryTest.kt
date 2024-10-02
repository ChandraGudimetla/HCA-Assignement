package com.example.hca

import com.example.hca.data.api.GithubApiService
import com.example.hca.data.model.Owner
import com.example.hca.data.model.Repository
import com.example.hca.data.repository.GithubRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class GithubRepositoryTest {

    private lateinit var githubRepository: GithubRepository
    private lateinit var githubApiService: GithubApiService

    @Before
    fun setUp() {
        githubApiService = mock()

        githubRepository = GithubRepository(githubApiService)
    }

    @Test
    fun `getRepositories should return a list of repositories`() = runTest(UnconfinedTestDispatcher()) {
        val username = "testUser"
        val page = 1
        val mockRepositories = listOf(
            Repository(
                id = 1,
                name = "Repo1",
                description = "Test Repo 1",
                forksCount = 10,
                language = "Kotlin",
                owner = Owner(id = 101, login = "testOwner"),
                stargazersCount = 100
            ),
            Repository(
                id = 2,
                name = "Repo2",
                description = "Test Repo 2",
                forksCount = 5,
                language = "Java",
                owner = Owner(id = 102, login = "testOwner2"),
                stargazersCount = 50
            )
        )

        `when`(githubApiService.getRepositories(username, page)).thenReturn(mockRepositories)

        val result = githubRepository.getRepositories(username, page)

        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Repo1", result[0].name)
        verify(githubApiService).getRepositories(username, page) // Verifies that the API was called with correct arguments
    }

    @Test(expected = Exception::class)
    fun `getRepositories should throw an exception if apiService throws an exception`(): Unit = runBlocking {
        val username = "testUser"
        val page = 1

        `when`(githubApiService.getRepositories(username, page)).thenThrow(RuntimeException("API error"))

        githubRepository.getRepositories(username, page)

    }
}