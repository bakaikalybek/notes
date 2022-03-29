package kg.bakai.notes.domain.use_case

import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.deleteNoteById(id)
            if (response.isSuccessful) {
                emit(Resource.Success(true))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error in delete note use case"))
        }
    }
}