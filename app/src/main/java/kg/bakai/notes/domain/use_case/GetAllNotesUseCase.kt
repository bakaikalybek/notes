package kg.bakai.notes.domain.use_case

import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    operator fun invoke(): Flow<Resource<List<Note>?>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.getNotesList()
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }
}