package com.pmovil.webservices.dao

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pmovil.webservices.database.AppCollection
import com.pmovil.webservices.model.Product

class ProductDAOFirestore(val firestore: FirebaseFirestore) : ProductDAO {

    private var mCollection: CollectionReference

    companion object {
        private const val TAG = "ProductDAOFirestore"
    }

    init {
        mCollection = firestore.collection(AppCollection.PRODUCT)
    }

    override fun getAll(): List<Product> {
        TODO("This method can not be implemented because Firestore works Async")
    }

    override fun findByCode(code: Int): Product {
        TODO("This method can not be implemented because Firestore works Async")
    }

    override fun insertAll(vararg products: Product) {
        products.forEach { product ->
            mCollection.document(product.code.toString()).set(product)
                .addOnSuccessListener {
                    Log.d(TAG, "Product added: ${product.code}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding product: ${product.code}", e)
                }
        }
    }

    override fun update(product: Product) {
        mCollection.document(product.code.toString()).set(product)
            .addOnSuccessListener {
                Log.d(TAG, "Product updated: ${product.code}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating product: ${product.code}", e)
            }
    }

    override fun delete(product: Product) {
        mCollection.document(product.code.toString()).delete()
    }

    override fun deleteAll() {
        firestore.collection(AppCollection.PRODUCT).get()
            .addOnSuccessListener {
                it.documents.forEach {
                    it.reference.delete()
                }
            }
    }
}