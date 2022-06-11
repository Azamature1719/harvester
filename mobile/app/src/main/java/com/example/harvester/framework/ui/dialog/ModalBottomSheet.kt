package com.example.harvester.framework.ui.dialog


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harvester.R
import com.example.harvester.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet(private val buttonList: MutableList<BottomSheetButton>) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private val sheetButtonsAdapter = BottomSheetButtonsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        BottomSheetDialogBinding.inflate(inflater, container, false).also { _binding = it }
        return binding.root
    }

    // Делаем прозрачный фон у нашего лайаута
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    // Делаем так, чтобы по клику закрывалась форма фрагмента
    private fun makeButtonClickClosable(button: BottomSheetButton) : BottomSheetButton {
        val previousButtonClick = button.buttonClick
        button.buttonClick = object : ButtonClick {
            override fun onClick() {
                previousButtonClick?.onClick()
                dismiss()
            }
        }
        return button
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetButtons.adapter = sheetButtonsAdapter
        binding.bottomSheetButtons.layoutManager = LinearLayoutManager(requireContext())

        // Чтобы сделать закрытие по нажатию на формы
        for (i in buttonList.indices) {
            if (buttonList[i].isCloseSheetAfter)
                buttonList[i] = makeButtonClickClosable(buttonList[i])
        }

        sheetButtonsAdapter.addButtons(buttonList)
    }

    override fun onDestroyView() {
        binding.bottomSheetButtons.adapter = null
        _binding = null
        super.onDestroyView()

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}