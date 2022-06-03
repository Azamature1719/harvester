package com.example.harvester.framework.ui.main.allertview

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentManager
import com.example.harvester.framework.extensions.cornerBackground
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UIAllertAction : BottomSheetDialogFragment() {

    private var actions: Array<Pair<String, () -> Unit>> = emptyArray()
    private var title: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val divider = ShapeDrawable(RectShape())
        divider.paint.color = Color.LTGRAY
        divider.intrinsicHeight = 1

        val linearLayout = LinearLayout(requireContext())
        linearLayout.cornerBackground(Color.WHITE, 16)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = divider

        if(title != null) {
            val textView = TextView(requireContext())
            textView.text = title
            textView.gravity = Gravity.CENTER
            textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)

            linearLayout.addView(textView)
        }

        // MARK: - Инициализация кнопок.

        for(action in actions) {
            val actionView = TextView(requireContext())
            actionView.setTextColor(Color.BLACK)
            actionView.text = action.first
            actionView.gravity = Gravity.CENTER
            actionView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
            actionView.setOnClickListener {
                action.second();
                dismiss()
            }
            linearLayout.addView(actionView)
        }

        // MARK: - Инициализация разделительного элемента.

        val separatorView = TextView(requireContext())
        separatorView.height = 12

        // MARK: - Инициализация кнопки отмены.

        val cancelView = TextView(requireContext())
        cancelView.cornerBackground(Color.WHITE, 16)
        cancelView.setTextColor(Color.BLACK)
        cancelView.text = "Отмена"
        cancelView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        cancelView.gravity = Gravity.CENTER
        cancelView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 62)
        cancelView.setOnClickListener { dissmiss() }

        // MARK: - Инициализация родительского контейнера.

        val parentlinearLayout = LinearLayout(requireContext())
        parentlinearLayout.orientation = LinearLayout.VERTICAL
        parentlinearLayout.setPadding(20)
        parentlinearLayout.addView(linearLayout)
        parentlinearLayout.addView(separatorView)
        parentlinearLayout.addView(cancelView)

        return parentlinearLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun dissmiss() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()
    }

    fun setTitle(title: String) { this.title = title }
    fun addAction(title: String, block: () -> Unit) { this.actions += Pair(title, block) }
    fun show(manager: FragmentManager) { super.show(manager, UIAllertAction.TAG) }

    companion object {
        const val TAG = "UIAllertAction"
    }
}