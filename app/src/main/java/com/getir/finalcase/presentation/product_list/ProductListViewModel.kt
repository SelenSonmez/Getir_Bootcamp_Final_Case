package com.getir.finalcase.presentation.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getir.finalcase.common.domain.ViewState
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.ProductCategory
import com.getir.finalcase.domain.usecase.ProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productListUseCase: ProductListUseCase
) : ViewModel() {

    private val _uiStateProduct: MutableStateFlow<ViewState<List<ProductCategory>>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateProduct = _uiStateProduct.asStateFlow()

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
}
