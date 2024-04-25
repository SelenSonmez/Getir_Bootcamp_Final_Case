package com.getir.finalcase.presentation.cart

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.getir.finalcase.R
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.databinding.FragmentCartBinding
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.presentation.SharedProductViewModel
import com.getir.finalcase.presentation.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartItemAdapter
    private lateinit var suggestedProductListAdapter: ProductListAdapter

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

    private fun navigateToProductList() {
       val navController = findNavController()
       navController.navigate(FragmentCartDirections.actionCartFragmentToProductListFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.uiStateSuggestedProducts.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {
                    state.result.firstOrNull()?.products?.let { suggestedProductListAdapter.updateProducts(it) }
                }
                is ViewState.Error -> {
                    val errorMessage = state.error ?: "Unknown error occurred"
                }
            }
        })
        binding.apply {
            toolbar.toolbarTitle.text = getString(R.string.basket)
            toolbar.backButton.visibility = VISIBLE
            val color = ContextCompat.getColor(requireContext(),R.color.white)
            toolbar.deleteIcon.setImageTintList(ColorStateList.valueOf(color))
            toolbar.deleteIcon.visibility = VISIBLE

            toolbar.backButton.setOnClickListener {
                navigateToProductList()
            }
            viewModel.uiStateProductBasketTotal.observe(viewLifecycleOwner, Observer {totalAmount ->
                totalCartAmount.text = "₺ $totalAmount"
            })

            btnCompleteOrder.setOnClickListener {
                val totalAmount = viewModel.uiStateProductBasketTotal.value ?: "0.00"
                showCustomDialogBox("$totalAmount ₺ değerindeki Siparişiniz alındı. Teşekkürler \uD83D\uDE4F")
            }
        }

        viewModel.uiStateProductInBasket.observe(viewLifecycleOwner, Observer { productList ->
            adapter.updateProducts(productList)
        })

    }

    private fun showCustomDialogBox(totalAmount: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_alert_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Access the TextView in the custom layout and set the total amount
        val totalAmountTextView = dialog.findViewById<TextView>(R.id.message)
        totalAmountTextView.text = totalAmount

        dialog.findViewById<Button>(R.id.finishOrderBtn)?.setOnClickListener {
            viewModel.removeAllProductsFromCart()
            dialog.dismiss()
            navigateToProductList()
        }
        dialog.show()
    }
    private fun onAddButtonClick(product: Product) {
      viewModel.addProductToBasketIfFound(product)
    }
    private fun onMinusButtonClicked(product: Product) {
        viewModel.removeOrReduceProductFromBasket(product)
    }

    private fun setupRecyclerView() {
        adapter = CartItemAdapter(emptyList(), ::onAddButtonClick, ::onMinusButtonClicked)
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.productsRecyclerView.adapter = adapter

        suggestedProductListAdapter = ProductListAdapter(emptyList(), null, ::onAddButtonClick, ::onMinusButtonClicked )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = suggestedProductListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Release the binding to avoid memory leaks
        _binding = null
    }
}
