package kg.bakai.notes.presentation.features.add_note_feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kg.bakai.notes.R
import kg.bakai.notes.databinding.FragmentAddNoteBinding
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.presentation.dialogs.CustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private val addNoteViewModel: AddNoteViewModel by viewModels()
    val args: AddNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        observeState()
    }

    private fun bindViews() {
        binding.apply {
            btnSaveNote.setOnClickListener { saveNote(args.note) }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            etContent.addTextChangedListener {
                val count = it!!.length
                tvWordCount.text = "$count / 500"
            }
            if (args.note != null) {
                binding.etTitle.setText(args.note!!.title)
                binding.etContent.setText(args.note!!.content)
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            addNoteViewModel.addNoteState.collectLatest { state ->
                when {
                    state.isLoading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                    state.error.isNotBlank() -> {
                        binding.progressCircular.visibility = View.GONE
                        Snackbar
                            .make(binding.btnSaveNote, state.error, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
                            .setAction("Action", null).show()
                    }
                    state.isSuccessful -> {
                        binding.progressCircular.visibility = View.GONE
                        Snackbar
                            .make(binding.btnSaveNote, "Добавлено", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(android.R.color.holo_green_light))
                            .setAction("Action", null)
                            .show()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun saveNote(note: Note?) {
        val dialog = CustomDialog("Вы хотите сохранить заметку?", onSaveClick = object : CustomDialog.Callback {
            override fun onSaveClick() {
                addNoteViewModel.createNote(
                    id = note?.id ?: -1,
                    title = binding.etTitle.text.toString(),
                    content = binding.etContent.text.toString()
                )
            }
        })
        activity?.supportFragmentManager?.apply {
            dialog.show(this, "TAG")
        }
    }
}