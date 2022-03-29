package kg.bakai.notes.presentation.features.details_feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.DetailsState
import kg.bakai.notes.domain.use_case.DeleteNoteUseCase
import kg.bakai.notes.domain.use_case.GetNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
): ViewModel() {
    private val TAG = javaClass.canonicalName

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState: StateFlow<DetailsState> = _detailsState

    fun getNote(id: Int) {
        getNoteUseCase.invoke(id).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _detailsState.value = DetailsState(data = resource.data)
                }
                is Resource.Loading -> {
                    _detailsState.value = DetailsState(isLoading = true)
                }
                else -> {
                    _detailsState.value = DetailsState(error = resource.message!!)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteNote(id: Int) {
        deleteNoteUseCase.invoke(id).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _detailsState.value = DetailsState(isDeleteSuccess = true)
                }
                is Resource.Loading -> {
                    _detailsState.value = DetailsState(isLoading = true)
                }
                else -> {
                    _detailsState.value = DetailsState(error = resource.message!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}