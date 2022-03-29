package kg.bakai.notes.presentation.features.notes_feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.NotesListState
import kg.bakai.notes.domain.use_case.GetAllNotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase
): ViewModel() {
    private val TAG = javaClass.canonicalName

    private val _notesState: MutableStateFlow<NotesListState> = MutableStateFlow(NotesListState())
    val notesState: StateFlow<NotesListState> = _notesState

    fun getAllNotes() {
        Log.i(TAG, "click: getAllNotes")
        getAllNotesUseCase.invoke().onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _notesState.value = NotesListState(data = resource.data ?: emptyList())
                }
                is Resource.Loading -> {
                    _notesState.value = NotesListState(isLoading = true)
                }
                else -> {
                    _notesState.value = NotesListState(error = resource.message!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}