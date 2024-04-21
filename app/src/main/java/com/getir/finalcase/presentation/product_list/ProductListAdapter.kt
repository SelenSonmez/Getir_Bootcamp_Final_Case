package com.getir.finalcase.presentation.product_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.getir.finalcase.databinding.ItemProductBinding
import com.getir.finalcase.domain.model.Product

class ProductListAdapter(
    private var dataSet: List<Product>,
    private val onItemClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    // ViewHolder class holds references to views within each item of the RecyclerView
    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind method to populate views with data
        fun bind(product: Product,onItemClick: (Product) -> Unit) {
            // Use data binding to set data to views
            binding.apply {
                textViewAttribute.text = product.attribute
                textViewPrice.text = product.priceText.toString()

                // If the product name exceeds length 30
                val maxProductNameLength = 30
                val displayName = if (product.name!!.length > maxProductNameLength) {
                    "${product.name.substring(0, maxProductNameLength)}..."
                } else {
                    product.name // Keep the original name
                }
                textViewName.text = displayName

                // Load image using Glide library
                Glide.with(root)
                    .load(product.imageURL)
                    .into(productImage)

                root.setOnClickListener { onItemClick(product) }
            }
        }
    }

    // Create ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate layout using data binding
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Bind data to views in each item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = dataSet[position]
        holder.bind(product,onItemClick)
    }

    // Update dataset with new list of products
    fun updateProducts(productList: List<Product>) {
        dataSet = productList
        notifyDataSetChanged()
    }

    // Return the size of dataset
    override fun getItemCount() = dataSet.size
}
