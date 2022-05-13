package com.example.harvester.framework.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harvester.R
import com.example.harvester.databinding.MainFragmentBinding
import com.example.harvester.framework.ui.camera.HarvesterCameraFragment
import com.example.harvester.framework.ui.main.allertview.UIAllertAction
import com.example.harvester.framework.ui.state.AppState
import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.api.RestService
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }

        if(viewModel.getProcessingStatus() == ProcessingStatusType.processing){
            when(viewModel.getProcessingMode()){
                ProcessingModeType.revision -> viewModel.startRevision()
                ProcessingModeType.collection -> viewModel.startCollection()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(viewModel.modeIsNone()) {
            when (item.itemId) {
                android.R.id.home -> {
                    val allertAction = UIAllertAction()
                    allertAction.addAction("Сверить товары") { viewModel.startRevision() }
                    allertAction.addAction("Собрать товары") { viewModel.startCollection() }
                    allertAction.show(requireActivity().getSupportFragmentManager())
                }
                R.id.toolbar_settings -> {
                    val allertAction = UIAllertAction()
                    allertAction.setTitle("App ID: ABCD-4534")
                    allertAction.addAction("Загрузить товары") { viewModel.uploadProducts() }
                    allertAction.addAction("Удалить товары") { viewModel.clearDatabase() }
                    allertAction.show(requireActivity().getSupportFragmentManager())
                }
            }
        }
        else {
            when(item.itemId){
                android.R.id.home -> {
                    val allertAction = UIAllertAction()
                    allertAction.addAction("Выгрузить документ") { viewModel.sendDocument() }
                    allertAction.addAction("Очистить документ") { viewModel.clearDocument() }
                    allertAction.show(requireActivity().getSupportFragmentManager())
                }
                R.id.toolbar_scan -> {
                    startScanning()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(appState: AppState){
        when(appState){
            is AppState.ProductsUploaded -> {
                Toast.makeText(requireContext(), "Товары загружены", Toast.LENGTH_SHORT).show()
            }
            is AppState.ProductsDeleted -> {
                Toast.makeText(requireContext(), "Товары удалены", Toast.LENGTH_SHORT).show()
            }
            is AppState.Revision -> {
                adapter = ProductsRecyclerView(ProcessingModeType.revision)
                binding.productsRecyclerView.adapter = adapter
                binding.productsRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter.setItems(appState.listOfProducts)
                setToolbarTitle("Сверка")
            }
            is AppState.Collection -> {
                adapter = ProductsRecyclerView(ProcessingModeType.collection)
                binding.productsRecyclerView.adapter = adapter
                binding.productsRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter.setItems(appState.listOfProducts)
                setToolbarTitle("Сбор товаров")
            }
            is AppState.NoData -> {
                Toast.makeText(requireContext(), "В базе данных товаров нет", Toast.LENGTH_SHORT).show()
            }
            is AppState.DocumentCleaned -> {
                setToolbarTitle("Harvester")
                adapter.setItems(mutableListOf())
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Документ очищен", Toast.LENGTH_SHORT).show()
            }
            is AppState.DocumentSent -> {
                setToolbarTitle("Harvester")
                adapter.setItems(mutableListOf())
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Документ выгружен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setToolbarTitle(newTitle: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = newTitle
    }

    private fun startScanning() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCameraFragment()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequestCode
            )
        }
    }

    private fun openCameraFragment() {
        activity?.supportFragmentManager?.commit {
            replace(R.id.camera_fragment, HarvesterCameraFragment())
                .addToBackStack("")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraFragment()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivityForResult(intent, cameraPermissionRequestCode)
            }
        }
    }

    private val cameraPermissionRequestCode = 1

    companion object {
        fun newInstance() = MainFragment()
    }
}