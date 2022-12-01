package com.affordable.api

import com.affordable.data.network.google.NearPlaceModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("place/nearbysearch/json")
    suspend fun nearbySearch(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("types") types: String,
        @Query("key") key: String,
    ): Response<NearPlaceModel>

}