package com.getir.finalcase.presentation.cart

import androidx.fragment.app.Fragment
import com.getir.finalcase.presentation.product_details.ProductDetailsFragmentDirections


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.getir.finalcase.R
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.databinding.FragmentCartBinding
import com.getir.finalcase.databinding.FragmentProductListBinding
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.presentation.SharedProductViewModel
import com.getir.finalcase.presentation.product_list.ProductListAdapter
import com.getir.finalcase.presentation.product_list.ProductListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentCart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartItemAdapter
   // private val viewModel: CartViewModel by viewModels()
    private val viewModel: SharedProductViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and initialize the binding
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

   /* private fun navigateToProductList() {
        val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToProductListFragment()
        findNavController().navigate(action)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        //fetchBasket()
        //observeCart()
        // Populate the UI with the product details
        binding.apply {
            toolbar.toolbarTitle.text = getString(R.string.basket)
            toolbar.backButton.visibility = VISIBLE

            /*toolbar.backButton.setOnClickListener {
                navigateToProductList()
            }*/

        }
        viewModel.uiStateProductInBasket.observe(viewLifecycleOwner, Observer { state ->
            Log.v("deneme", state.size.toString())
        })
    }
  /*  private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateBasketProducts.collect { state ->
                when (state) {
                    is ViewState.Loading -> {
                    }
                    is ViewState.Success -> {
                        val basketProducts = state.result
                        basketProducts.let { adapter.updateProducts(it) }
                    }
                    is ViewState.Error -> {
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


    private fun fetchBasket() {
        viewModel.getAllBasketProducts()
    }*/
  private fun onAddButtonClick(product: Product) {
      //viewModel.getAllBasketProducts()
  }
    private fun setupRecyclerView() {
        adapter = CartItemAdapter(emptyList(), ::onAddButtonClick)
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.productsRecyclerView.adapter = adapter
    }

        override fun onDestroyView() {
        super.onDestroyView()
        // Release the binding to avoid memory leaks
        _binding = null
    }
}
