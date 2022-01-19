package com.codelabs.state.advancedcoroutine.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codelabs.state.MainActivity
import com.codelabs.state.R
import com.codelabs.state.advancedcoroutine.PlantListViewModel
import com.codelabs.state.advancedcoroutine.PlantRepository
import com.codelabs.state.advancedcoroutine.utils.Injector
import com.codelabs.state.databinding.FragmentPlantListBinding
import com.codelabs.state.sunflower.ui.PlantAdapter
import com.google.android.material.snackbar.Snackbar

class PlantListFragment : Fragment() {

    private val viewModel: PlantListViewModel by viewModels {
        Injector.providePlantListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlantListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        // show the spinner when [spinner] is true
        viewModel.spinner.observe(viewLifecycleOwner) { show ->
            binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
        }

        // Show a snackbar whenever the [snackbar] is updated a non-null value
        viewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }

        val adapter = PlantAdapter()
        binding.plantList.adapter = adapter
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_zone -> {
                updateData()
                true
            }

            R.id.leave_this_activity_to_firebaseviewpager_activity -> {
               //Leave this activity to another tutorial activity
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUi(adapter: PlantAdapter) {
       /* viewModel.plants.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
        }*/
        viewModel.plantsUsingFlow.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
        }
    }

    private fun updateData() {
        with(viewModel) {
            if (isFiltered()) {
                clearGrowZoneNumber()
            } else {
                setGrowZoneNumber(9)
            }
        }
    }
}

/**
 * Factory for creating a [PlantListViewModel] with a constructor that takes a [PlantRepository].
 */
class PlantListViewModelFactory(
    private val repository: PlantRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = PlantListViewModel(repository) as T
}
