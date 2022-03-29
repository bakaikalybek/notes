package kg.bakai.notes.presentation.features.notes_feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kg.bakai.notes.R
import kg.bakai.notes.databinding.FragmentNotesBinding
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.presentation.adapters.NotesAdapter
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private val notesViewModel: NotesViewModel by viewModels()
    private val adapter = NotesAdapter { onNoteClick(it)  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        observeState()
    }

    private fun bindViews() {
        notesViewModel.getAllNotes()
        binding.apply {
            btnAddNote.setOnClickListener {
                findNavController().navigate(R.id.action_notesFragment_to_addNoteFragment)
            }
            rvNotes.adapter = adapter
            swipeLayout.setOnRefreshListener { notesViewModel.getAllNotes() }
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            notesViewModel.notesState.collectLatest { state ->
                when {
                    state.isLoading -> {
                        binding.rvNotes.visibility = View.GONE
                        binding.swipeLayout.isRefreshing = true
                    }
                    state.error.isNotBlank() -> {
                        binding.rvNotes.visibility = View.GONE
                        binding.swipeLayout.isRefreshing = false
                        Snackbar
                            .make(binding.btnAddNote, state.error, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
                            .setAction("Action", null)
                            .show()
                    }
                    else -> {
                        binding.rvNotes.visibility = View.VISIBLE
                        binding.swipeLayout.isRefreshing = false
                        adapter.submitItems(state.data)
                    }
                }
            }
        }
    }

    private fun onNoteClick(note: Note) {
        val action = NotesFragmentDirections.actionNotesFragmentToDetailsFragment(note.id!!)
        findNavController().navigate(action)
    }
}