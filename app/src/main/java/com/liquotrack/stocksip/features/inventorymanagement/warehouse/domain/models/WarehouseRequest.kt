package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models

/**
 * Data class representing a Warehouse entity.
 *
 * @param id Unique identifier for the warehouse.
 * @param name Name of the warehouse.
 * @param street Street address of the warehouse.
 * @param city City where the warehouse is located.
 * @param district District where the warehouse is located.
 * @param postalCode Postal code of the warehouse location.
 * @param country Country where the warehouse is located.
 * @param temperatureMin Minimum temperature the warehouse can maintain.
 * @param temperatureMax Maximum temperature the warehouse can maintain.
 * @param capacity Total capacity of the warehouse in cubic meters.
 */
data class WarehouseRequest(
    val name: String,
    val street: String,
    val city: String,
    val district: String,
    val postalCode: String,
    val country: String,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val capacity: Double
)
