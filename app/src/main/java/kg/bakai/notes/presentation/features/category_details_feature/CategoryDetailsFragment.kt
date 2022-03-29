package kg.bakai.notes.presentation.features.category_details_feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kg.bakai.notes.R
import kg.bakai.notes.databinding.FragmentCategoryDetailsBinding
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.presentation.adapters.NotesAdapter
import kg.bakai.notes.presentation.features.notes_feature.NotesFragmentDirections
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CategoryDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCategoryDetailsBinding
    private val args: CategoryDetailsFragmentArgs by navArgs()
    private val categoryDetailsViewModel: CategoryDetailsViewModel by viewModels()
    private val adapter = NotesAdapter { onNoteClick(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        observeState()
    }

    private fun bindViews() {
        if (args.category.id == 0) {
            categoryDetailsViewModel.getNotes()
        }
        binding.apply {
            rvCategoryNotes.adapter = adapter
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            toolbar.title = args.category.name
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            categoryDetailsViewModel.notesState.collectLatest { state ->
                when {
                    state.isLoading -> {
                        binding.rvCategoryNotes.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    state.error.isNotBlank() -> {
                        binding.rvCategoryNotes.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                    }
                    else -> {
                        binding.rvCategoryNotes.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        adapter.submitItems(state.data)
                    }
                }
            }
        }
    }

    private fun onNoteClick(note: Note) {
        val action = CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToDetailsFragment(note.id!!)
        findNavController().navigate(action)
    }

}