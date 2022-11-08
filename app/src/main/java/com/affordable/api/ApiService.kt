package com.affordable.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun welcome(
        @Url fullUrl: String,
    ): Response<String>

}