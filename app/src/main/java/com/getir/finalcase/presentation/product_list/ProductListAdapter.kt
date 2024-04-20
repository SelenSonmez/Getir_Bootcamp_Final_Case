package com.getir.finalcase.presentation.product_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.getir.finalcase.R
import com.getir.finalcase.domain.model.Product

class ProductListAdapter(private var dataSet: List<Product>) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewAttribute: TextView
        val textViewPrice: TextView
        val imageView: ImageView


        init {
            textViewName = view.findViewById(R.id.textViewName)
            textViewAttribute = view.findViewById(R.id.textViewAttribute)
            textViewPrice = view.findViewById(R.id.textViewPrice)
            imageView = view.findViewById(R.id.productImage)

        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val product = dataSet[position]
        Log.v("selen","deneme")
        product.name?.let { Log.v("selen", it) }
        viewHolder.textViewName.text = product.name
        viewHolder.textViewAttribute.text = product.attribute
        viewHolder.textViewPrice.text = product.priceText.toString()
        Glide.with(viewHolder.itemView.context)
            .load(product.imageURL)
            .into(viewHolder.imageView)
    }


    fun updateProducts(productList: List<Product>) {
        dataSet = productList
        notifyDataSetChanged()
    }

    // Return the size of dataset
    override fun getItemCount() = dataSet.size


}


