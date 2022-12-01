package com.affordable.repository

import com.affordable.api.ApiService

class MainRepository(private val service: ApiService) {

    suspend fun findNearbyPlaces(location: String, radius: String, types: String, apiKey: String) =
        service.nearbySearch(location, radius, types, apiKey)

}