package com.pmovil.webservices.dao

import androidx.room.*
import com.pmovil.webservices.model.Product

@Dao
interface ProductDAODatabase : ProductDAO {
    @Query("SELECT * FROM product")
    override fun getAll(): List<Product>

    @Query("SELECT * FROM product WHERE code = :code")
    override fun findByCode(code: Int): Product

    @Insert
    override fun insertAll(vararg products: Product)

    @Update
    override fun update(product: Product)

    @Delete
    override fun delete(product: Product)

    @Query("DELETE FROM product")
    override fun deleteAll()
}