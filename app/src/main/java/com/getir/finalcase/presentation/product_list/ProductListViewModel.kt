package com.getir.finalcase.presentation.product_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.data.local.repository.BasketProductRepository
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.ProductCategory
import com.getir.finalcase.domain.usecase.ProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase,
    private val basketProductRepository: BasketProductRepository
) : ViewModel() {

    private val _uiStateProduct: MutableStateFlow<ViewState<List<ProductCategory>>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateProduct = _uiStateProduct.asStateFlow()

    private val _uiStateBasketAddition: MutableStateFlow<ViewState<String>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateBasketAddition = _uiStateBasketAddition.asStateFlow()


    fun getAllProducts() {
        productListUseCase.execute()
            .map { baseResponse ->
                when (baseResponse) {
                    is BaseResponse.Success -> {
                        ViewState.Success(baseResponse.data)
                    }
                    is BaseResponse.Error -> ViewState.Error(baseResponse.message)
                    else -> ViewState.Error("Unexpected response received")
                }
            }
            .onEach { data ->
                _uiStateProduct.emit(data)
            }
            .catch { error ->
                _uiStateProduct.emit(ViewState.Error(error.message ?: "Unknown error occurred"))
            }
            .launchIn(viewModelScope)
    }
    fun addProductToBasketIfFound(product: Product) {
        viewModelScope.launch {
            val response = basketProductRepository.addProductToBasketIfFound(product)
            when (response) {
                is BaseResponse.Success -> {
                    _uiStateBasketAddition.emit(ViewState.Success("Product added to basket successfully"))
                }
                is BaseResponse.Error -> {
                    _uiStateBasketAddition.emit(ViewState.Error(response.message))
                }
            }
        }
    }

}
