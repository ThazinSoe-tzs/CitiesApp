package com.thazin.citiesapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.thazin.citiesapp.R
import com.thazin.citiesapp.common.AppConstants
import com.thazin.citiesapp.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var latitude = 0.0
    private var longitude = 0.0
    private var cityName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = requireNotNull(intent?.extras)
        with(arguments){
            latitude = getDouble(AppConstants.LATITUDE)
            longitude = getDouble(AppConstants.LONGITUDE)
            cityName = getString(AppConstants.CITY).toString()
        }

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val cityLatLng = LatLng(latitude, longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(cityLatLng)
                .title(cityName)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, AppConstants.DEFAULT_ZOOM))
    }
}