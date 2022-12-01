package com.affordable.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.affordable.data.network.google.NearPlaceModel
import com.affordable.repository.MainRepository
import com.affordable.utility.NetworkHelper
import com.affordable.utility.Resource
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _nearPlaceResponse = MutableLiveData<Resource<NearPlaceModel>>()
    val nearPlaceResponse: LiveData<Resource<NearPlaceModel>>
        get() = _nearPlaceResponse


    fun findNearByPlaces(lattitude: String, longitude: String) {

        val location = lattitude + "," + longitude
        val radius = "5000"
        val types = "store"
        val apikey = "AIzaSyDCsRDB8T0VjnDw389k6JPoyo86nj5XBeE"

        viewModelScope.launch {
            _nearPlaceResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.findNearbyPlaces(
                    location,
                    radius,
                    types,
                    apikey
                ).let {
                    if (it.isSuccessful) {
                        _nearPlaceResponse.postValue(Resource.success(it.body()))
                    } else _nearPlaceResponse.postValue(
                        Resource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            } else _nearPlaceResponse.postValue(Resource.error("No internet connection", null))
        }

    }

}