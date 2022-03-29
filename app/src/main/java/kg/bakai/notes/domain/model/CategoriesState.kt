package kg.bakai.notes.domain.model

data class CategoriesState(
    val isLoading: Boolean = false,
    val data: List<Category> = emptyList(),
    val error: String = ""
)
