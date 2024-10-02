package com.example.hca.data.model

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long,
    val name: String,
    val description: String,
    val language: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    val owner: Owner
)

data class Owner(
    val id: Long,
    val login: String
)
