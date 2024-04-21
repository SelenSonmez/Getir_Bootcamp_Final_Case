package com.getir.finalcase.presentation.product_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.getir.finalcase.R
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.databinding.FragmentProductListBinding
import com.getir.finalcase.domain.model.Product
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var bindingProduct: FragmentProductListBinding

    private lateinit var adapter: ProductListAdapter
    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeProductList()
        fetchProducts()

        binding.toolbar.toolbarTitle.text = getString(R.string.title_products)
        binding.toolbar.containerBasket.visibility = VISIBLE
    }
    fun onAddButtonClick(product: Product) {
        // Update the count of the specific item
        //viewModel.increaseItemCount(product)
    }
    private fun setupRecyclerView() {
        adapter = ProductListAdapter(emptyList(),::onItemClicked)
        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.productsRecyclerView.adapter = adapter
    }

    private fun onItemClicked(product: Product) {
        navigateToProductDetails(product)
    }
    private fun navigateToProductDetails(product: Product) {
        val action = ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }
    private fun observeProductList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateProduct.collect { state ->
                when (state) {
                    is ViewState.Loading -> {
                        binding.loadingProgressBar.visibility = View.VISIBLE
                    }
                    is ViewState.Success -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        val productList = state.result
                        productList[0].products?.let { adapter.updateProducts(it) }
                    }
                    is ViewState.Error -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        // Show error message to user
                        Snackbar.make(
                            requireView(),
                            "Error: ${state.error}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun fetchProducts() {
        viewModel.getAllProducts()
    }
}
