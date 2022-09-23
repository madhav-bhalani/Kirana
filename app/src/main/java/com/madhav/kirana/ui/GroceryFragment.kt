package com.madhav.kirana.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.madhav.kirana.adapter.GroceryAdapter
import com.madhav.kirana.adapter.GroceryHeaderAdapter
import com.madhav.kirana.data.GroceryRoomDatabase
import com.madhav.kirana.databinding.FragmentGroceryBinding

class GroceryFragment : Fragment() {

    private lateinit var binding: FragmentGroceryBinding

    private val viewModel: GroceryViewModel by viewModels {
        GroceryViewModelFactory(
            GroceryRoomDatabase.getDatabase(requireContext().applicationContext).groceryDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroceryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        // Navigate to Add Item dialog
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                GroceryFragmentDirections.actionGroceryFragmentToAddItemDialogFragment()
            )
        }

        registerForContextMenu(binding.recyclerViewGrocery)
    }

    /**
     * Sets up the RecyclerView Adapter for displaying list of grocery items.
     */
    private fun setupAdapter() {
        val groceryAdapter = GroceryAdapter { viewModel.deleteItem(it) }
        val headerAdapter = GroceryHeaderAdapter()
        val concatAdapter = ConcatAdapter(headerAdapter, groceryAdapter)
        binding.recyclerViewGrocery.adapter = concatAdapter

        // Listen for grocery data
        viewModel.items.observe(viewLifecycleOwner) { groceryAdapter.submitList(it) }
    }
}