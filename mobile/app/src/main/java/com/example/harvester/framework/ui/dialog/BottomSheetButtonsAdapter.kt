package com.example.harvester.framework.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.harvester.R

class BottomSheetButtonsAdapter : RecyclerView.Adapter<BottomSheetButtonsAdapter.BottomSheetButtonItem>() {

    private val buttonList = mutableListOf<BottomSheetButton>()

    fun addButton(newButton: BottomSheetButton) {
        buttonList.add(newButton)
        notifyItemInserted(buttonList.size-1)
    }

    fun addButtons(newButtons: List<BottomSheetButton>) {
        buttonList.addAll(newButtons)
        notifyItemRangeChanged(0, newButtons.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetButtonItem {
        return BottomSheetButtonItem(LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: BottomSheetButtonItem, position: Int) {
        holder.bind(buttonList[position])
    }

    override fun getItemCount(): Int = buttonList.size

    inner class BottomSheetButtonItem(view: View) : RecyclerView.ViewHolder(view) {

        private val buttonView = view.findViewById<AppCompatTextView>(R.id.button)

        fun bind(button: BottomSheetButton) {
            buttonView.text = button.text
            itemView.setOnClickListener { button.buttonClick?.onClick() }
        }

    }
}