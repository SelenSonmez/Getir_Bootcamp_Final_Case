package com.getir.finalcase.presentation.product_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.getir.finalcase.R
import com.getir.finalcase.databinding.FragmentProductDetailsBinding
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.presentation.SharedProductViewModel
import com.getir.finalcase.presentation.product_list.ProductListFragmentDirections


class ProductDetailsFragment : Fragment() {

    private var binding: FragmentProductDetailsBinding? = null
    private val viewModel: SharedProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and initialize the binding
        val fragmentBinding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    private fun navigateToProductList() {
        val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToProductListFragment()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the arguments passed from the navigation action
        val args: ProductDetailsFragmentArgs by navArgs()
        val product: Product = args.product


        // Populate the UI with the product details
        binding?.apply {
            showAddBasketButton(this,product)
            textProductName.text = product.name
            textViewPrice.text = product.priceText
            textProductAttribute.text = product.attribute
            quantityText.text = product.amount.toString()

            toolbar.toolbarTitle.text = getString(R.string.title_product_details)
            toolbar.backButton.visibility = VISIBLE

            toolbar.backButton.setOnClickListener {
                navigateToProductList()
            }

            // Load the product image using Glide
            Glide.with(requireContext())
                .load(product.imageURL)
                .placeholder(R.drawable.default_product_image)
                .into(productImage)

            addButton.setOnClickListener {
                quantityText.text = (product.amount+1).toString()
                viewModel.addProductToBasketIfFound(product)
            }

            addToBasket.setOnClickListener {
                quantityText.text =  (product.amount +1).toString()
                viewModel.addProductToBasketIfFound(product)
                quantityLayout.visibility = VISIBLE
                addToBasket.visibility = GONE
            }
            minusButton.setOnClickListener {
                quantityText.text = (product.amount-1).toString()
                viewModel.removeOrReduceProductFromBasket(product)
                if(product.amount < 1) {
                    quantityLayout.visibility = GONE
                    addToBasket.visibility = VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Release the binding to avoid memory leaks
        binding = null
    }
    private fun showAddBasketButton(binding: FragmentProductDetailsBinding, product: Product) {
        val itemCount = product.amount
        binding.apply {
            if(itemCount > 0) {
                quantityLayout.visibility = VISIBLE
                addToBasket.visibility = GONE
            } else {
                quantityLayout.visibility = GONE
                addToBasket.visibility = VISIBLE
            }
        }

    }
}
