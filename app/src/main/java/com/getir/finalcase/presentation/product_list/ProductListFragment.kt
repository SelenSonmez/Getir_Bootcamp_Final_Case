package com.getir.finalcase.presentation.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.getir.finalcase.R
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.databinding.FragmentProductListBinding
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.presentation.SharedProductViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var suggestedProductListAdapter: ProductListAdapter
    private val viewModel: SharedProductViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fragmentCreated()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        // Observe the UI state of products
        viewModel.uiStateProducts.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    binding.loadingProgressBar.visibility = View.GONE
                    state.result.firstOrNull()?.products?.let { adapter.updateProducts(it) }
                    val productList = state.result
                }
                is ViewState.Error -> {
                    val errorMessage = state.error ?: "Unknown error occurred"
                }
            }
        })

        binding.toolbar.containerBasket.visibility = VISIBLE
        viewModel.uiStateSuggestedProducts.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    // Update UI with the list of products
                    binding.loadingProgressBar.visibility = View.GONE
                    state.result.firstOrNull()?.products?.let { suggestedProductListAdapter.updateProducts(it) }
                    val productList = state.result
                }
                is ViewState.Error -> {
                    // Show error message
                    val errorMessage = state.error ?: "Unknown error occurred"
                }
            }
        })
        setupRecyclerViews()

    }

    private fun setupToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.title_products)

        viewModel.uiStateProductBasketTotal.observe(viewLifecycleOwner, Observer { state ->
            binding.toolbar.totalAmount.text = "₺ $state"
        })
            binding.toolbar.containerBasket.setOnClickListener {
                if(binding.toolbar.totalAmount.text.toString() != "₺ 0.00"){
                    val action = ProductListFragmentDirections.actionProductListFragmentToCartFragment()
                    findNavController().navigate(action)
                }  else{
                    Snackbar.make(
                        requireView(),
                        "No item is in cart",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setupRecyclerViews() {
        adapter = ProductListAdapter(emptyList(), ::onItemClicked, ::onAddButtonClick, :: onDeleteButtonClick)
        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.productsRecyclerView.adapter = adapter

        suggestedProductListAdapter = ProductListAdapter(emptyList(), ::onItemClicked, ::onAddButtonClick, ::onDeleteButtonClick)
        binding.productHorizontalRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.productHorizontalRecyclerView.adapter = suggestedProductListAdapter
    }

    private fun fetchSuggestedProducts() {
        viewModel.getSuggestedProducts()
    }

    private fun onItemClicked(product: Product) {
        val action = ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

     private fun onAddButtonClick(product: Product) {
        viewModel.addProductToBasketIfFound(product)
    }

    private fun onDeleteButtonClick(product: Product) {
        viewModel.removeOrReduceProductFromBasket(product)
    }
}
