package kg.bakai.notes.domain.use_case

import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    private val TAG = javaClass.canonicalName

    operator fun invoke(id: Int): Flow<Resource<Note>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.getNoteById(id)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }
}