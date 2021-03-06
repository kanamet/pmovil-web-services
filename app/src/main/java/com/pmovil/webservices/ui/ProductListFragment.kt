package com.pmovil.webservices.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmovil.persistencia.interfaces.OnItemClickListener
import com.pmovil.webservices.R
import com.pmovil.webservices.dao.ProductDAO
import com.pmovil.webservices.database.AppDatabase
import com.pmovil.webservices.model.Product
import kotlinx.android.synthetic.main.fragment_product_list.view.*
import kotlinx.android.synthetic.main.item_product.view.*

class ProductListFragment : Fragment() {

    companion object {
        private const val TAG = "ProductListFragment"
    }

    private lateinit var mView: View
    private lateinit var db: AppDatabase
    private lateinit var productDAO: ProductDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        mView = inflater.inflate(R.layout.fragment_product_list, container, false)
        mView.add_item_btn.setOnClickListener { addProduct() }

        db = AppDatabase.getInstance(context)
        productDAO = db.productDAO()

        loadProducts()

        return mView
    }

    private fun loadProducts() {
        loadProducts(productDAO.getAll())
    }

    private fun loadProducts(productsList: List<Product>) {
        val productAdapter = ProductAdapter()
        productAdapter.data = productsList
        productAdapter.onItemClickListener = onItemClickListener

        mView.recyclerView.adapter = productAdapter
        mView.recyclerView.layoutManager = LinearLayoutManager(context)

        if (productsList.isNotEmpty()) {
            mView.empty_messages_text.visibility = View.GONE
        } else {
            mView.empty_messages_text.visibility = View.VISIBLE
        }

        Log.i(TAG, "Total Products: ${productsList.size}")
    }

    private fun addProduct() {
        val bundle = bundleOf(
            "action" to "ADD_PRODUCT"
        )

        findNavController().navigate(R.id.navigation_product_details, bundle)
    }

    private val onItemClickListener = object : OnItemClickListener<Product> {
        override fun onItemClick(product: Product) {
            val bundle = bundleOf(
                "action" to "EDIT_PRODUCT",
                "product_code" to product.code
            )

            findNavController().navigate(R.id.navigation_product_details, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_product_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_option_populate_database -> {
                val products = arrayOf(
                    Product(1, "Chocoramo", "Ponqué cubierto de chocolate", 12, 1400.0),
                    Product(2, "Milo 250 gr", "Fusión de malta, vitaminas y minerales", 6, 8300.0),
                    Product(3, "Lavaplatos 720 ml", "Efectivo contra la grasa", 4, 5700.0),
                    Product(4, "Avena 350 gr", "Completa y saludable", 8, 6250.0),
                    Product(5, "Te Hindu", "Antioxidante natural", 3, 10090.0)
                )
                productDAO.deleteAll()
                productDAO.insertAll(*products)
                loadProducts()
            }

            R.id.menu_option_delete_data -> {
                productDAO.deleteAll()
                loadProducts()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private class ProductAdapter() :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

        var data: List<Product> = emptyList()
        var onItemClickListener: OnItemClickListener<Product>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)

            return ProductViewHolder(
                itemView,
                itemView.product_code_text,
                itemView.product_name_text
            )
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = data[position]
            holder.codeTxt.text = "${product.code}"
            holder.nameTxt.text = product.name
            holder.view.setOnClickListener {
                onItemClickListener?.onItemClick(product)
            }
        }

        class ProductViewHolder(val view: View, val codeTxt: TextView, val nameTxt: TextView) :
            RecyclerView.ViewHolder(view)

    }
}