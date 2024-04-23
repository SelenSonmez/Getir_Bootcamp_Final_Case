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
import com.getir.finalcase.domain.usecase.SuggestedProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val suggestedProductUseCase: SuggestedProductUseCase,
    private val basketProductRepository: BasketProductRepository
) : ViewModel() {


    private val _uiStateProduct: MutableSharedFlow<ViewState<List<ProductCategory>>> =
        MutableSharedFlow()
    val uiStateProduct = _uiStateProduct.asSharedFlow()

    private val _uiStateSuggestedProduct: MutableSharedFlow<ViewState<List<ProductCategory>>> =
        MutableSharedFlow()
    val uiStateSuggestedProduct = _uiStateSuggestedProduct.asSharedFlow()

    private val _uiStateBasketAddition: MutableSharedFlow<ViewState<String>> =
        MutableSharedFlow()
    val uiStateBasketAddition = _uiStateBasketAddition.asSharedFlow()

    private val _menuVisibility: MutableSharedFlow<ViewState<List<ProductCategory>>> =
        MutableSharedFlow()
    val menuVisibility = _menuVisibility.asSharedFlow()

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
            val response = basketProductRepository.addProductToBasket(product)
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

    fun getSuggestedProducts() {
        suggestedProductUseCase.execute()
            .map { baseResponse ->
                when (baseResponse) {
                    is BaseResponse.Success -> {
                        Log.v("selen",baseResponse.data.toString())
                        ViewState.Success(baseResponse.data)
                    }
                    is BaseResponse.Error -> ViewState.Error(baseResponse.message)
                    else -> ViewState.Error("Unexpected response received")
                }
            }
            .onEach { data ->
                _uiStateSuggestedProduct.emit(data)
            }
            .catch { error ->
                _uiStateSuggestedProduct.emit(ViewState.Error(error.message ?: "Unknown error occurred"))
            }
            .launchIn(viewModelScope)
    }

}
