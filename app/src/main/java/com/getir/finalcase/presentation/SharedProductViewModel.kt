package com.getir.finalcase.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.data.local.repository.BasketProductRepository
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.ProductCategory
import com.getir.finalcase.domain.usecase.ProductListUseCase
import com.getir.finalcase.domain.usecase.SuggestedProductUseCase
import com.getir.finalcase.ext.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedProductViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase,
    private val suggestedProductUseCase: SuggestedProductUseCase,
    private val basketProductRepository: BasketProductRepository
) : ViewModel() {

    val uiStateProducts = MutableLiveData<ViewState<List<ProductCategory>>>()

    val uiStateSuggestedProducts = MutableLiveData<ViewState<List<ProductCategory>>>()

    val uiStateProductInBasket = MutableLiveData<List<Product>>()

     fun getAllProducts() {
         viewModelScope.launch {
             productListUseCase.execute()
                 .map { baseResponse ->
                     when (baseResponse) {
                         is BaseResponse.Success -> {
                             Log.v("base",baseResponse.data.toString())
                             ViewState.Success(baseResponse.data)
                         }
                         is BaseResponse.Error -> ViewState.Error(baseResponse.message)
                         else -> ViewState.Error("Unexpected response received")
                     }
                 }
                 .onEach { data ->
                     uiStateProducts.value = data
                 }
                 .catch { error ->
                     uiStateProducts.value =
                         ViewState.Error(error.message ?: "Unknown error occurred")
                 }
                 .collect {
                     loadSaveData()

                 }

         }
    }

    suspend fun loadSaveData() {
        // Eğer bir product savede varsa amountunu savedeki olanla eşitle
        val currentProducts =  mutableListOf<Product>()

        uiStateProducts.value.let { viewState ->
            // Check if the viewState is a Success
            if (viewState is ViewState.Success) {
                // Access the product list
                viewState.result.firstOrNull()?.products?.forEach { product ->
                    // Call the repository to get product details by ID
                        val response = basketProductRepository.getBasketProductById(product.id)
                    if(response is BaseResponse.Success) {
                        if(response.data != null ) {
                            product.amount = response.data.amount

                        }
                        currentProducts.add(product)
                    }
                }
            }
        }
        uiStateProductInBasket.value = currentProducts
        uiStateProducts.notifyObserver()
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
                uiStateSuggestedProducts.value = data
            }
            .catch { error ->
                uiStateSuggestedProducts.value = ViewState.Error(error.message ?: "Unknown error occurred")
            }
            .launchIn(viewModelScope)
    }

     fun addProductToBasketIfFound(product: Product) {
         viewModelScope.launch {
             val response = basketProductRepository.addProductToBasket(product)
             if(response is BaseResponse.Success) {
                 product.amount += 1
                 uiStateProducts.notifyObserver()
                 ViewState.Success("Product added to basket successfully")
             }
         }

    }

   /* fun getAllBasketProducts() {
        viewModelScope.launch {
            try {
                uiStateBasketProducts.value = ViewState.Loading
                basketProductRepository.getAllBasketProducts().collect { baseResponse ->
                    when (baseResponse) {
                        is BaseResponse.Success -> {
                            uiStateBasketProducts.value = ViewState.Success(baseResponse.data)
                        }
                        is BaseResponse.Error -> {
                            uiStateBasketProducts.value = ViewState.Error(baseResponse.message ?: "Unknown error occurred")
                        }
                    }
                }
            } catch (e: Exception) {
                uiStateBasketProducts.value = ViewState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }*/

}
