package com.getir.finalcase.presentation

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.getir.finalcase.R
import com.getir.finalcase.databinding.ItemProductBinding
import com.getir.finalcase.domain.model.Product

class ProductListAdapter(
    private var dataSet: List<Product>,
    private val onItemClick: ((Product) -> Unit)?,
    private val onAddButtonClick: (Product) -> Unit,
    private val onDeleteButtonClick: (Product) -> Unit
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    // ViewHolder class holds references to views within each item of the RecyclerView
    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: Product,
            onItemClick: ((Product) -> Unit)?,
            onAddButtonClick: (Product) -> Unit,
            onDeleteButtonClick: (Product) -> Unit
        ) {
            binding.apply {
                if (product.amount > 0) {
                    enableAmountMenu(binding)
                } else {
                    disableAmountMenu(binding)
                }

                textViewAttribute.text = product.attribute
                textViewPrice.text = product.priceText.toString()
                textCount.text = product.amount.toString()

                // If the product name exceeds length 30 collapse text
                val maxProductNameLength = 15
                val displayName = if (product.name!!.length > maxProductNameLength) {
                    "${product.name.substring(0, maxProductNameLength)}..."
                } else {
                    product.name
                }
                textViewName.text = displayName

                // Load image using Glide library
                if (product.imageURL != null) {
                    Glide.with(root)
                        .load(product.imageURL)
                        .into(productImage)
                } else {
                    Glide.with(root)
                        .load(product.squareThumbnailURL)
                        .into(productImage)
                }

                addBtn.setOnClickListener {
                    onAddButtonClick(product)
                    binding.addBtn.setBackgroundResource(R.drawable.button_add_background_straight_bg)
                    enableAmountMenu(binding)
                }

                deleteBtn.setOnClickListener {
                    onDeleteButtonClick(product)
                }

                // Check if onItemClick is not null before setting click listener
                root.setOnClickListener { onItemClick?.invoke(product) }
            }
        }

        private fun enableAmountMenu(binding: ItemProductBinding) {
            binding.apply {
                addBtn.visibility = VISIBLE
                textCount.visibility = VISIBLE
                deleteBtn.visibility = VISIBLE
            }
        }

        private fun disableAmountMenu(binding: ItemProductBinding) {
            binding.apply {
                textCount.visibility = GONE
                deleteBtn.visibility = GONE
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
        holder.bind(product, onItemClick, onAddButtonClick, onDeleteButtonClick)
    }

    // Update dataset with new list of products
    fun updateProducts(productList: List<Product>) {
        dataSet = productList
        notifyDataSetChanged()
    }

    // Return the size of dataset
    override fun getItemCount() = dataSet.size
}

