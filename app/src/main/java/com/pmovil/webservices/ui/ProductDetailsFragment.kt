package com.pmovil.webservices.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.pmovil.webservices.R
import com.pmovil.webservices.dao.ProductDAO
import com.pmovil.webservices.dao.ProductDAODatabase
import com.pmovil.webservices.dao.ProductDAOFirestore
import com.pmovil.webservices.database.AppCollections
import com.pmovil.webservices.database.AppDatabase
import com.pmovil.webservices.model.Product
import kotlinx.android.synthetic.main.fragment_product_details.view.*

class ProductDetailsFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var db: AppDatabase
    private lateinit var productDAO: ProductDAO
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var mAction: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_product_details, container, false)

        mView.save_product_btn.setOnClickListener { saveProduct() }
        mView.delete_product_btn.setOnClickListener { deleteProduct() }

        db = AppDatabase.getInstance(context)
//        productDAO = db.productDAO()

        firebaseFirestore = FirebaseFirestore.getInstance()
        productDAO = ProductDAOFirestore(firebaseFirestore)

        arguments?.apply {
            mAction = getString("action", "")
            if (mAction == "EDIT_PRODUCT") {
                loadProduct(getInt("product_code"))
            }
        }

        return mView
    }

    private fun loadProduct(productCode: Int) {
        if (productDAO is ProductDAODatabase) {

            updateUI(productDAO.findByCode(productCode))

        } else if (productDAO is ProductDAOFirestore) {

            firebaseFirestore.collection(AppCollections.PRODUCT)
                .document(productCode.toString()).get()
                .addOnSuccessListener { document ->
                    updateUI(document.toObject(Product::class.java))
                }

        }
    }

    private fun updateUI(product: Product?) {
        mView.product_code_text.setText("${product?.code}")
        mView.product_name_text.setText(product?.name)
        mView.product_description_text.setText(product?.description)
        mView.product_quantity_text.setText("${product?.quantity}")
        mView.product_price_text.setText("${product?.price}")
    }

    private fun saveProduct() {

        val product = Product(
            mView.product_code_text.text.toString().toInt(),
            mView.product_name_text.text.toString(),
            mView.product_description_text.text.toString(),
            mView.product_quantity_text.text.toString().toInt(),
            mView.product_price_text.text.toString().toDouble()
        )

        when (mAction) {
            "ADD_PRODUCT" -> productDAO.insertAll(product)
            "EDIT_PRODUCT" -> productDAO.update(product)
        }

        findNavController().navigateUp()

        Toast.makeText(context, "Saving Product", Toast.LENGTH_SHORT).show()
    }

    private fun deleteProduct() {
        val product = Product(
            mView.product_code_text.text.toString().toInt(),
            mView.product_name_text.text.toString(),
            mView.product_description_text.text.toString(),
            mView.product_quantity_text.text.toString().toInt(),
            mView.product_price_text.text.toString().toDouble()
        )

        productDAO.delete(product)

        findNavController().navigateUp()

        Toast.makeText(context, "Deleting Product", Toast.LENGTH_SHORT).show()
    }

}