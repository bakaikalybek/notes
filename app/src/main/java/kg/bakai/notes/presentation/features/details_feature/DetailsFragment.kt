package kg.bakai.notes.presentation.features.details_feature

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kg.bakai.notes.R
import kg.bakai.notes.databinding.FragmentDetailsBinding
import kg.bakai.notes.databinding.FragmentNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var notesBinding: FragmentNotesBinding
    private val detailsViewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        notesBinding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        observeState(args.noteId)
    }

    private fun bindViews() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_delete -> {
                        detailsViewModel.deleteNote(args.noteId)
                    }
                    R.id.menu_edit -> {
                        editNote()
                    }
                }
                true
            }
        }
    }

    private fun observeState(id: Int) {
        detailsViewModel.getNote(id)
        lifecycleScope.launchWhenStarted {
            detailsViewModel.detailsState.collectLatest { state ->
                when {
                    state.isLoading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    state.isDeleteSuccess -> {
                        binding.progressBar.visibility = View.GONE
                        findNavController().navigateUp()
                        Toast.makeText(requireContext(), "Удалено", Toast.LENGTH_SHORT).show()
                    }
                    state.error.isNotBlank() -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar
                            .make(binding.toolbar, state.error, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
                            .show()
                    }
                    state.data != null -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNoteTitle.text = state.data.title
                        binding.tvNoteContent.text = state.data.content
                    }
                }
            }
        }
    }

    private fun editNote() {
        detailsViewModel.detailsState.value.data?.let { note ->
            val action = DetailsFragmentDirections.actionDetailsFragmentToAddNoteFragment(note = note)
            findNavController().navigate(action)
        }
    }

}