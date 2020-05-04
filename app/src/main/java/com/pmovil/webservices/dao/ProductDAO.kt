package com.pmovil.webservices.dao

import com.pmovil.webservices.model.Product

interface ProductDAO {
    fun getAll(): List<Product>

    fun findByCode(code: Int): Product

    fun insertAll(vararg products: Product)

    fun update(product: Product)

    fun delete(product: Product)

    fun deleteAll()
}