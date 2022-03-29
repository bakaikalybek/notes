package kg.bakai.notes.data.repository

import kg.bakai.notes.domain.model.Category
import kg.bakai.notes.domain.repository.CategoryRepository

class CategoryRepositoryImpl: CategoryRepository {
    override fun getCategoryList(): List<Category> {
        return listOf(
            Category(
                id = 0,
                name = "Общая"
            ),
            Category(
                id = 1,
                name = "Работа"
            )
        )
    }
}