package kg.bakai.notes.presentation.features.category_feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kg.bakai.notes.R
import kg.bakai.notes.databinding.FragmentCategoryBinding
import kg.bakai.notes.domain.model.Category
import kg.bakai.notes.presentation.adapters.CategoriesAdapter
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val adapter = CategoriesAdapter { category -> onCategoryClick(category)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        observeState()
    }

    private fun bindViews() {
        binding.apply {
            rvCategories.adapter = adapter
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            categoryViewModel.state.collectLatest { state ->
                when {
                    state.isLoading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    state.error.isNotBlank() -> {
                        Snackbar
                            .make(binding.root, state.error, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
                            .show()
                    }
                    state.data.isNotEmpty() -> {
                            adapter.submitItems(state.data)
                    }
                }
            }
        }
    }

    private fun onCategoryClick(category: Category) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToCategoryDetailsFragment(category = category)
        findNavController().navigate(action)
    }
}