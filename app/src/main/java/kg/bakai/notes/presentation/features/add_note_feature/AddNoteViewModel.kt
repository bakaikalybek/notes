package kg.bakai.notes.presentation.features.add_note_feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.AddNoteState
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.domain.model.NotesListState
import kg.bakai.notes.domain.use_case.CreateNoteUseCase
import kg.bakai.notes.domain.use_case.GetNoteUseCase
import kg.bakai.notes.domain.use_case.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
): ViewModel() {
    private val TAG = javaClass.canonicalName

    private val _addNoteState = MutableStateFlow(AddNoteState())
    val addNoteState: StateFlow<AddNoteState> = _addNoteState

    fun createNote(id: Int, title: String, content: String) {
        Log.i(TAG, "click: createNote")
        if (id == -1) {
            createNoteUseCase.invoke(title = title, content = content).onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _addNoteState.value = AddNoteState(isSuccessful = true)
                    }
                    is Resource.Loading -> {
                        _addNoteState.value = AddNoteState(isLoading = true)
                    }
                    else -> {
                        _addNoteState.value = AddNoteState(error = resource.message!!)
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            updateNoteUseCase.invoke(id = id,title = title, content = content).onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _addNoteState.value = AddNoteState(isSuccessful = true)
                    }
                    is Resource.Loading -> {
                        _addNoteState.value = AddNoteState(isLoading = true)
                    }
                    else -> {
                        _addNoteState.value = AddNoteState(error = resource.message!!)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}