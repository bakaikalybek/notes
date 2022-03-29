package kg.bakai.notes.domain.model

data class DetailsState(
    val isLoading: Boolean = false,
    val isDeleteSuccess: Boolean = false,
    val data: Note? = null,
    val error: String = ""
)
