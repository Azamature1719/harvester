package com.example.harvester.framework.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harvester.R
import com.example.harvester.databinding.MainFragmentBinding
import com.example.harvester.framework.ui.camera.HarvesterCameraFragment
import com.example.harvester.framework.ui.dialog.BottomSheetButton
import com.example.harvester.framework.ui.dialog.ButtonClick
import com.example.harvester.framework.ui.dialog.ModalBottomSheet
import com.example.harvester.framework.ui.main.allertview.UIAllertAction
import com.example.harvester.framework.ui.state.AppState
import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: ProductsRecyclerView

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
        viewModel.setProcessingMode()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private val databaseMenuButtons = mutableListOf(
        BottomSheetButton("Загрузить товары", object : ButtonClick {
            override fun onClick( ) { viewModel.downloadTable() }}),
        BottomSheetButton("Удалить товары", object : ButtonClick {
            override fun onClick( ) { viewModel.deleteTable() }}, true),
        BottomSheetButton("Настройки подключения", object : ButtonClick {
            override fun onClick( ) { }})
    )

    private val documentMenuButtons = mutableListOf(
        BottomSheetButton("Выгрузить документ", object : ButtonClick {
            override fun onClick( ) { viewModel.sendDocument() }}),
        BottomSheetButton("Очистить документ", object : ButtonClick {
            override fun onClick( ) { viewModel.clearDocument() }}, true),
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(viewModel.modeIsNone()) {
            if(item.itemId == android.R.id.home) {
                val modalBottomSheet = ModalBottomSheet(databaseMenuButtons)
                modalBottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
            }
        }
        else {
            when(item.itemId){
                R.id.toolbar_settings -> {
                    val modalBottomSheet = ModalBottomSheet(documentMenuButtons)
                    modalBottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
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
            is AppState.TableDownloaded -> {
                viewModel.fillDatabaseWith(appState.table)
            }
            is AppState.DatabaseFilled -> {
                viewModel.setProcessingMode()
            }
            is AppState.NoData -> {
                Toast.makeText(requireContext(), "В базе данных товаров нет", Toast.LENGTH_SHORT).show()
            }
            is AppState.TableDeleted -> {
                Toast.makeText(requireContext(), "Товары удалены", Toast.LENGTH_SHORT).show()
            }
            is AppState.Revision -> {
                setToolbarTitle("Сверка")
                setItems(appState.listOfProducts)
            }
            is AppState.Collection -> {
                setToolbarTitle("Сбор товаров")
                setItems(appState.listOfProducts)
            }
            is AppState.DocumentCleaned -> {
                setToolbarTitle("Харвестер")
                adapter.setItems(mutableListOf())
                adapter.notifyItemChanged(1)
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Документ очищен", Toast.LENGTH_SHORT).show()
            }
            is AppState.DocumentSent -> {
                setToolbarTitle("Харвестер")
                adapter.setItems(mutableListOf())
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Документ выгружен", Toast.LENGTH_SHORT).show()
            }
            is AppState.ErrorOccured -> {
                Toast.makeText(requireContext(), appState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setItems(listOfProducts: MutableList<ProductInfoDTO>){
        adapter = ProductsRecyclerView(ProcessingModeType.revision)
        binding.productsRecyclerView.adapter = adapter
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setItems(listOfProducts)
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