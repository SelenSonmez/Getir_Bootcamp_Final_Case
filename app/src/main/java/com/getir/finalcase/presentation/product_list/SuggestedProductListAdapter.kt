package com.getir.finalcase.presentation.product_list

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.getir.finalcase.databinding.ItemProductBinding
import com.getir.finalcase.domain.model.Product

class SuggestedProductListAdapter(
    private var dataSet: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onAddButtonClick: (Product) -> Unit,
    private val onDeleteButtonClick: (Product) -> Unit

) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    // ViewHolder class holds references to views within each item of the RecyclerView
    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind method to populate views with data
        fun bind(product: Product,onItemClick: (Product) -> Unit, onAddButtonClick: (Product) ->Unit) {
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

                addBtn.setOnClickListener {
                    // Call the onAddButtonClick callback when the add button is clicked
                    onAddButtonClick(product)
                    addBtn.visibility = VISIBLE
                    textCount.visibility = VISIBLE
                    deleteBtn.visibility = VISIBLE

                }

                root.setOnClickListener { onItemClick(product) }
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListAdapter.ViewHolder {
        // Inflate layout using data binding
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductListAdapter.ViewHolder(binding)
    }

    // Update dataset with new list of products
    fun updateProducts(productList: List<Product>) {
        dataSet = productList
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        val product = dataSet[position]
        holder.bind(product,onItemClick,onAddButtonClick, onDeleteButtonClick)
    }


}
