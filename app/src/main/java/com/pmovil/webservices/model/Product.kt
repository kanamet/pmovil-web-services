package com.pmovil.webservices.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val code: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "quantity") var quantity: Int,
    @ColumnInfo(name = "price") var price: Double
) {
    constructor() : this(0, "", "", 0, 0.0)
}