package com.thazin.citiesapp.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thazin.citiesapp.R
import com.thazin.citiesapp.ui.adapter.CityAdapter
import com.thazin.citiesapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var cityAdapter: CityAdapter

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(searchStr: CharSequence?, start: Int, before: Int, count: Int) {
            cityAdapter.filter.filter(searchStr.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_search.addTextChangedListener(textWatcher)
        progress_bar.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this)
        rv_cities.layoutManager = linearLayoutManager

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getCityList().observe(this, Observer {
            cityList ->
            progress_bar.visibility = View.GONE
            cityAdapter = CityAdapter(cityList, this)
            rv_cities.adapter = cityAdapter
        })
    }
}