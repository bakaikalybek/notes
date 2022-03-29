package kg.bakai.notes.domain.repository

import kg.bakai.notes.domain.model.Category

interface CategoryRepository {
    fun getCategoryList(): List<Category>
}