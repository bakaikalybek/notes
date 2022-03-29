package kg.bakai.notes.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kg.bakai.notes.R
import kg.bakai.notes.domain.model.Category
import kg.bakai.notes.domain.model.Note

class CategoriesAdapter(private val callback: (category: Category) -> Unit): RecyclerView.Adapter<CategoriesAdapter.NoteViewHolder>() {
    private val notes = mutableListOf<Category>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.rv_category_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun submitItems(list: List<Category>) {
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tv_category_title)
        private val main: ConstraintLayout = itemView.findViewById(R.id.layout_category)

        fun bind(category: Category) {
            title.text = category.name
            main.setOnClickListener { callback(category) }
        }
    }
}