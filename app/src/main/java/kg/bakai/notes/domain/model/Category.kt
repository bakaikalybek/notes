package kg.bakai.notes.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int? = null,
    val name: String
): Parcelable
