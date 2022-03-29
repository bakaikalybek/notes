package kg.bakai.notes.presentation.features.category_feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.CategoriesState
import kg.bakai.notes.domain.use_case.GetAllCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
): ViewModel() {
    private val TAG = javaClass.canonicalName

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state

    init {
        getCategories()
    }

    private fun getCategories() {
        getAllCategoriesUseCase.invoke().onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _state.value = CategoriesState(data = resource.data!!)
                }
                is Resource.Error -> {
                    _state.value = CategoriesState(error = resource.message!!)
                }
                else -> {
                    _state.value = CategoriesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}