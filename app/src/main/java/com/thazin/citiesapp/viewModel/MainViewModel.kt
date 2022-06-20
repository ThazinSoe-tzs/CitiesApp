package com.thazin.citiesapp.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.thazin.citiesapp.data.FileUtils
import com.thazin.citiesapp.data.City

class MainViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        Log.i(MainViewModel::class.simpleName, "MainViewModelCreated")
    }

    fun getCityList() : MutableLiveData<List<City>> {
        return MutableLiveData<List<City>>(FileUtils.getCities(context))
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(MainViewModel::class.simpleName, "onCleared: GameViewModel destroyed!")
    }
}