package com.example.hca.data.repository

import com.example.hca.data.api.GithubApiService
import com.example.hca.data.model.Repository
import javax.inject.Inject

class GithubRepository @Inject constructor(private val apiService: GithubApiService) {
    suspend fun getRepositories(username: String, page: Int): List<Repository> {
        return apiService.getRepositories(username = username, page = page)
    }
}