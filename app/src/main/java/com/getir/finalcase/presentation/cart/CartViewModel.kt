package com.getir.finalcase.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.data.local.repository.BasketProductRepository
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.usecase.ProductListUseCase
import com.getir.finalcase.domain.usecase.SuggestedProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase,
    private val suggestedProductUseCase: SuggestedProductUseCase,
    private val basketProductRepository: BasketProductRepository
) : ViewModel() {

    private val _uiStateBasketProducts: MutableStateFlow<ViewState<List<Product>>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateBasketProducts: StateFlow<ViewState<List<Product>>> = _uiStateBasketProducts

    fun getAllBasketProducts() {
        viewModelScope.launch {
            basketProductRepository.getAllBasketProducts()
                .collect { baseResponse ->
                    when (baseResponse) {
                        is BaseResponse.Success -> {
                            Log.v("selen",baseResponse.data.toString())
                            _uiStateBasketProducts.value = ViewState.Success(baseResponse.data)
                        }
                        is BaseResponse.Error -> {
                            _uiStateBasketProducts.value = ViewState.Error(baseResponse.message)
                        }
                    }
                }
        }
    }
}