package com.getir.finalcase.presentation.product_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.getir.finalcase.data.local.database.entity.ProductEntity
import com.getir.finalcase.databinding.ItemProductBinding
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.ProductCategory

class ProductListAdapter(
    private var dataSet: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onAddButtonClick: (Product) -> Unit,
    private val onDeleteButtonClick: (Product) -> Unit
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    // ViewHolder class holds references to views within each item of the RecyclerView
    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind method to populate views with data
        fun bind(product: Product,onItemClick: (Product) -> Unit, onAddButtonClick: (Product) ->Unit, onDeleteButtonClick: (Product) -> Unit) {
            binding.apply {
                if(product.amount > 0){
                    enableAmountMenu(binding)
                }else{
                    disableAmountMenu(binding)
                }

                textViewAttribute.text = product.attribute
                textViewPrice.text = product.priceText.toString()
                textCount.text = product.amount.toString()
                Log.v("selennn",product.amount.toString())
                // If the product name exceeds length 30
                val maxProductNameLength = 15
                val displayName = if (product.name!!.length > maxProductNameLength) {
                    "${product.name.substring(0, maxProductNameLength)}..."
                } else {
                    product.name // Keep the original name
                }
                textViewName.text = displayName

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


                addBtn.setOnClickListener {
                    // Call the onAddButtonClick callback when the add button is clicked
                    onAddButtonClick(product)
                    enableAmountMenu(binding)

                }

                deleteBtn.setOnClickListener {
                    onDeleteButtonClick(product)
                }

                root.setOnClickListener { onItemClick(product) }
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
        holder.bind(product,onItemClick,onAddButtonClick,onDeleteButtonClick)
    }

    // Update dataset with new list of products
    fun updateProducts(productList: List<Product>) {
        dataSet = productList
        notifyDataSetChanged()
    }

    // Return the size of dataset
    override fun getItemCount() = dataSet.size
}
