package kg.bakai.notes.domain.use_case

import kg.bakai.notes.common.Resource
import kg.bakai.notes.domain.model.Category
import kg.bakai.notes.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
)  {
    private val TAG = javaClass.canonicalName

    operator fun invoke(): Flow<Resource<List<Category>>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.getCategoryList()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }
}