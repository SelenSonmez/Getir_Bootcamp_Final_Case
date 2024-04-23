package com.getir.finalcase.presentation.product_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var suggestedProductListAdapter: ProductListAdapter
    private val viewModel: SharedProductViewModel by activityViewModels()

    //private val viewModel: ProductListViewModel by viewModels()


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
        //observeViewModel()
        fetchProducts()

        fetchSuggestedProducts()

        // Observe the UI state of products
        viewModel.uiStateProducts.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    // Update UI with the list of products
                    binding.loadingProgressBar.visibility = View.GONE
                    state.result.firstOrNull()?.products?.let { adapter.updateProducts(it) }
                    val productList = state.result
                }
                is ViewState.Error -> {
                    // Show error message
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
        binding.toolbar.containerBasket.setOnClickListener {
            val action = ProductListFragmentDirections.actionProductListFragmentToCartFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerViews() {
        adapter = ProductListAdapter(emptyList(), ::onItemClicked, ::onAddButtonClick)
        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.productsRecyclerView.adapter = adapter

        suggestedProductListAdapter = ProductListAdapter(emptyList(), ::onItemClicked, ::onAddButtonClick)
        binding.productHorizontalRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.productHorizontalRecyclerView.adapter = suggestedProductListAdapter
    }

    /*private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateProduct.collect { state ->
                when (state) {
                    is ViewState.Loading -> binding.loadingProgressBar.visibility = View.VISIBLE
                    is ViewState.Success -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        state.result.firstOrNull()?.products?.let { adapter.updateProducts(it) }
                    }
                    is ViewState.Error -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "Error: ${state.error}", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateSuggestedProduct.collect { state ->
                when (state) {
                    is ViewState.Loading -> binding.loadingProgressBar.visibility = View.VISIBLE
                    is ViewState.Success -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        state.result.firstOrNull()?.products?.let { suggestedProductListAdapter.updateProducts(it) }
                    }
                    is ViewState.Error -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "Error: ${state.error}", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateBasketAddition.collect { state ->
                when (state) {
                    is ViewState.Loading -> binding.loadingProgressBar.visibility = View.VISIBLE
                    is ViewState.Success -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        Snackbar.make(requireView(), state.result, Snackbar.LENGTH_SHORT).show()
                    }
                    is ViewState.Error -> Snackbar.make(requireView(), "Error: ${state.error}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.menuVisibility.collectLatest {

            }
        }

    }*/

    private fun fetchProducts() {
        viewModel.getAllProducts()
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
}
