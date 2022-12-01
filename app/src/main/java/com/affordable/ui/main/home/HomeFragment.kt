package com.affordable.ui.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.affordable.base.BaseFragment
import com.affordable.data.network.google.NearPlaceModel
import com.affordable.data.network.google.Result
import com.affordable.databinding.FragmentHomeBinding
import com.affordable.utility.Status
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val TAG = HomeFragment::class.java.name

    lateinit var activity: AppCompatActivity

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var homeRecyclerviewAdapter: HomeRecyclerviewAdapter

    private var originalNearPlaceModelList: ArrayList<Result> = ArrayList()

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        checkLocationPermission()
    }


    override fun initListener() {

        setUpObservers()

        with(binding!!) {

            swiperefresh.setOnRefreshListener {
                fetchDataOnStart(forceRefresh = true)
                swiperefresh.isRefreshing = false
            }

            homeRecyclerviewAdapter = HomeRecyclerviewAdapter(requireContext(),
                object : HomeRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(model: NearPlaceModel) {
                    }

                })

            recommendationRecyclerview.adapter = homeRecyclerviewAdapter

            filterItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    filterPlease(newText)

                    return false
                }

            })
        }
    }

    private fun setUpObservers() {
        viewModel.nearPlaceResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { data ->
                        // do with data
                        Log.v(TAG, data.results.get(0).name)
                        originalNearPlaceModelList = data.results as ArrayList<Result>
                        homeRecyclerviewAdapter.setData(originalNearPlaceModelList)
                        setTotalStoresText(originalNearPlaceModelList)
                    }
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showToastShort(it.message.toString())
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun filterPlease(newText: String?) {

        homeRecyclerviewAdapter.setData(performFiltering(newText))

    }

    fun performFiltering(constraint: CharSequence?): ArrayList<Result> {

        val charSearch = constraint.toString()

        if (charSearch.isEmpty()) {
            setTotalStoresText(originalNearPlaceModelList)
            return originalNearPlaceModelList
        } else {

            val resultList = ArrayList<Result>()

            originalNearPlaceModelList.forEach {
                if (it.name.contains(constraint.toString(), ignoreCase = true)) {
                    resultList.add(it)
                }
            }

            setTotalStoresText(resultList)

            return resultList
        }

    }

    private fun setTotalStoresText(list: ArrayList<Result>) {
        binding?.totalStoresFounded?.text =
            list.size.toString() + " stores based on your preferences"
    }

    private fun checkLocationPermission() {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    requestCodeLocationPermission
                )
            } else {
                fetchDataOnStart()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLatitudeLongitude(callback: (String, String) -> Unit) {
        gpsTracker = GpsTracker(requireContext())
        if (gpsTracker!!.canGetLocation()) {
            userLattitude = gpsTracker!!.getLatitude().toString()
            userLongitude = gpsTracker!!.getLongitude().toString()
            Log.v(TAG, userLattitude + " " + userLongitude)
            callback(userLattitude, userLongitude)
        } else {
            gpsTracker!!.showSettingsAlert()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            requestCodeLocationPermission -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    showToastShort("Permission Granted")
                    fetchDataOnStart()
                    return
                } else {
                    checkLocationPermission()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            }
        }
    }

    private fun fetchDataOnStart(forceRefresh: Boolean = false) {
        if (userLattitude.isEmpty() || userLattitude.equals("0.0") || userLongitude.isEmpty() || userLongitude.equals(
                "0.0"
            )
        ) {
            getLatitudeLongitude { lat, lng ->
                //Call APi
                viewModel.findNearByPlaces(lat, lng)
            }
        } else if (forceRefresh) {
            if (userLattitude.isEmpty() || userLattitude.equals("0.0") || userLongitude.isEmpty() || userLongitude.equals(
                    "0.0"
                )
            ) {
                showToastShort("Try Again, Location not found")
                return
            }
            getLatitudeLongitude { lat, lng ->
                //Call APi
                viewModel.findNearByPlaces(lat, lng)
            }
        }
    }
}