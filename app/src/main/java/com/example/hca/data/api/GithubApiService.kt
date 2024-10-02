package com.example.hca.data.api

import com.example.hca.data.model.Repository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users/{username}/repos")
    suspend fun getRepositories(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): List<Repository>
}