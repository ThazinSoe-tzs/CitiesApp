package com.thazin.citiesapp.data

data class City(
    val country: String,
    val name: String,
    val _id: Long,
    val coord: Coordinate
)
data class Coordinate (
    val lon: Double,
    val lat: Double
)
