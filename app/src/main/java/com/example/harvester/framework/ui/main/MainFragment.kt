package com.example.harvester.framework.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harvester.R
import com.example.harvester.databinding.MainFragmentBinding
import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.api.RestService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "Table of goods"

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: ProductsRecyclerView
    private lateinit var btnGetProducts:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)
//        viewModel.getLiveData().observe(viewLifecycleOwner) { viewModel.fillDatabase(it as String)}
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            adapter = ProductsRecyclerView()
            binding.productsRecyclerView.adapter = adapter
            binding.productsRecyclerView.layoutManager = LinearLayoutManager(context)
            adapter.setItems(it as MutableList<ProductInfoDTO>)
        }
//        viewModel.getProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home-> {
                val dialog = BottomSheetDialog(requireContext()) /// ЧТО ЭТО БЛ ТАКОЕ?
                val view = layoutInflater.inflate(R.layout.main_bottomsheet, null)
                val btnUpdate = view.findViewById<Button>(R.id.get_document)
                val btnClear = view.findViewById<Button>(R.id.clear_document)
                btnUpdate.setOnClickListener {
                    viewModel.getProducts()
                }
                btnClear.setOnClickListener {
                    viewModel.clearDataBase()
                }
                dialog.setContentView(view)
                dialog.setCancelable(true)
                dialog.show()
            }
            R.id.toolbar_scan -> {
                Toast.makeText(super.getContext(), "Scan", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbar_settings-> {
                Toast.makeText(super.getContext(), "Database settings", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}

class ModalBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.main_bottomsheet, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.get_document).setOnClickListener {
        }
        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
    }
}