package com.getir.finalcase.presentation.cart

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import com.getir.finalcase.presentation.product_details.ProductDetailsFragmentDirections


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.getir.finalcase.R
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.databinding.CustomAlertDialogBinding
import com.getir.finalcase.databinding.FragmentCartBinding
import com.getir.finalcase.databinding.FragmentProductListBinding
import com.getir.finalcase.databinding.ItemCartProductTileBinding
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.ext.notifyObserver
import com.getir.finalcase.presentation.SharedProductViewModel
import com.getir.finalcase.presentation.product_list.ProductListAdapter
import com.getir.finalcase.presentation.product_list.ProductListFragmentDirections
import com.getir.finalcase.presentation.product_list.ProductListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentCart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartItemAdapter

    private var _dialogBinding: CustomAlertDialogBinding? = null
    private val dialogBinding get() = _dialogBinding!!


    private val viewModel: SharedProductViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and initialize the binding
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        _dialogBinding =  CustomAlertDialogBinding.inflate(inflater,container,false)

        return binding.root
    }

   private fun navigateToProductList() {
       val navController = findNavController()
       navController.navigate(FragmentCartDirections.actionCartFragmentToProductListFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
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
                totalCartAmount.text = totalAmount
            })

            btnCompleteOrder.setOnClickListener {
                showCustomDialogBox(dialogBinding)
            }
        }

        viewModel.uiStateProductInBasket.observe(viewLifecycleOwner, Observer { productList ->
            adapter.updateProducts(productList)
        })

    }

    private fun showCustomDialogBox(dialogBinding: CustomAlertDialogBinding) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_alert_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<Button>(R.id.finishOrderBtn)?.setOnClickListener {
            viewModel.removeAllProductsFromCart()
            dialog.dismiss()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Release the binding to avoid memory leaks
        _binding = null
    }
}
