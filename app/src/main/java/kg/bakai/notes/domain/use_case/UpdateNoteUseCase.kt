package kg.bakai.notes.domain.use_case

import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    private val TAG = javaClass.canonicalName

    operator fun invoke(id: Int, title: String, content: String): Flow<Resource<Note>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.updateNote(id, title, content)
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