package com.getir.finalcase.presentation.cart

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.getir.finalcase.databinding.FragmentCartBinding
import com.getir.finalcase.databinding.ItemCartProductTileBinding
import com.getir.finalcase.databinding.ItemProductBinding
import com.getir.finalcase.domain.model.Product

class CartItemAdapter(
    private var dataSet: List<Product>,
    private val onAddButtonClick: (Product) -> Unit,
    private val onMinusButtonClick: (Product) -> Unit

) :
    RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    // ViewHolder class holds references to views within each item of the RecyclerView
    class ViewHolder(private val binding: ItemCartProductTileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind method to populate views with data
        fun bind(product: Product, onAddButtonClick: (Product) ->Unit, onMinusButtonClick: (Product) ->Unit) {
            // Use data binding to set data to views
            binding.apply {
                textViewAttribute.text = product.attribute
                textViewPrice.text = product.priceText.toString()
                quantityText.text = product.amount.toString()

                    if(product.amount > 1){
                    minusButton.visibility = VISIBLE
                    deleteButton.visibility = GONE
                }else{
                    minusButton.visibility = GONE
                    deleteButton.visibility = VISIBLE
                }

                deleteButton.setOnClickListener {
                    onMinusButtonClick(product)
                }

                // If the product name exceeds length 30
                val maxProductNameLength = 15
                val displayName = if (product.name!!.length > maxProductNameLength) {
                    "${product.name.substring(0, maxProductNameLength)}..."
                } else {
                    product.name // Keep the original name
                }
                textViewProductName.text = displayName

                // Load image using Glide library
                if(product.imageURL != null ){
                    Glide.with(root)
                        .load(product.imageURL)
                        .into(productImage)
                }else{
                    Glide.with(root)
                        .load(product.squareThumbnailURL)
                        .into(productImage)
                }


                addButton.setOnClickListener {
                    // Call the onAddButtonClick callback when the add button is clicked
                    onAddButtonClick(product)
                }

                minusButton.setOnClickListener {
                    // Call the onAddButtonClick callback when the minus button is clicked
                    onMinusButtonClick(product)
                }
            }
        }
    }

    // Create ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate layout using data binding
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartProductTileBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Bind data to views in each item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = dataSet[position]
        holder.bind(product,onAddButtonClick,onMinusButtonClick)
    }

    // Update dataset with new list of products
    fun updateProducts(productList: List<Product>) {
        dataSet = productList
        notifyDataSetChanged()
    }

    // Return the size of dataset
    override fun getItemCount() = dataSet.size
}
