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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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

    val uiStateProductInBasket = MutableLiveData<List<Product>>(emptyList())

    val uiStateProductBasketTotal = MutableLiveData<String>()

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
                     loadSaveData(uiStateProducts)
                     loadSaveData(uiStateSuggestedProducts)
                     calculateAmount()
                 }
         }
    }

    suspend fun loadSaveData(liveData: MutableLiveData<ViewState<List<ProductCategory>>>) {
        // Eğer bir product savede varsa amountunu savedeki olanla eşitle
        val currentProducts =  mutableListOf<Product>()
        liveData.value.let { viewState ->
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
                        uiStateProductInBasket.value = uiStateProductInBasket.value?.plus(product)
                        currentProducts.add(product)
                    }
                }
            }
        }
        liveData.notifyObserver()
        uiStateProductInBasket.notifyObserver()
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
         val currentProducts =  mutableListOf<Product>()


         uiStateProductInBasket.value?.let {
             for(previousProduct in it){
                 currentProducts.add(previousProduct)
             }
         }

         viewModelScope.launch {
             product.amount += 1
             val responseIfDuplicateProduct = basketProductRepository.getBasketProductById(product.id)
             if(responseIfDuplicateProduct is BaseResponse.Error){
                 val response = basketProductRepository.addProductToBasket(product)
                 if(response is BaseResponse.Success) {
                     currentProducts.add(product)
                     uiStateProductInBasket.value = currentProducts
                     ViewState.Success("Product added to basket successfully")
                 }
             }else{
                basketProductRepository.increaseProductCount(product.id)
             }
             uiStateProducts.notifyObserver()
             uiStateSuggestedProducts.notifyObserver()
             uiStateProductInBasket.notifyObserver()
             calculateAmount()
         }
    }

    fun removeOrReduceProductFromBasket(product: Product) {
        val currentProducts =  mutableListOf<Product>()

        uiStateProductInBasket.value?.let {
            for(previousProduct in it){
                currentProducts.add(previousProduct)
            }
        }
        viewModelScope.launch {
            product.amount -= 1
            val response = basketProductRepository.removeProductFromBasket(product)
            if(response is BaseResponse.Success) {
                if(product.amount < 1) {
                    currentProducts.remove(product)
                }
                uiStateProductInBasket.value = currentProducts
                uiStateProducts.notifyObserver()
                uiStateSuggestedProducts.notifyObserver()
                uiStateProductInBasket.notifyObserver()
                calculateAmount()

                ViewState.Success("Product amount reduced from basket successfully")
            }
        }
    }
    fun removeAllProductsFromCart() {
        viewModelScope.launch {
            val response = basketProductRepository.deleteAllBasketProducts()
            if(response is BaseResponse.Success) {
                uiStateProductInBasket.value?.forEach {product ->
                    run {
                        product.amount = 0
                    }
                }
                uiStateProductInBasket.value = emptyList()
                uiStateProductBasketTotal.value = 0.0.toString()
                uiStateProducts.notifyObserver()
                uiStateSuggestedProducts.notifyObserver()
                uiStateProductInBasket.notifyObserver()
                uiStateProductBasketTotal.notifyObserver()
                ViewState.Success("Products are removed from the basket")
            }else{
                ViewState.Error("Products are couldn't removed from the basket")
            }
        }
    }

    fun calculateAmount() {
        var count = 0.0
        uiStateProductInBasket.value.let {
            it?.forEach { product ->
                Log.v("welene",product.toString())
                if(product.price != null) {
                    Log.v("selendene"," $product ${product.price} * ${product.amount}")
                    count += product.price * product.amount
                }
            }
            uiStateProductBasketTotal.value = String.format("%.2f",count)
        }
    }
}
