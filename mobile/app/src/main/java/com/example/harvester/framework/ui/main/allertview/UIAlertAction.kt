package ru.scancity.enterprise.ui.allert.action

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.example.harvester.R
import com.example.harvester.framework.ui.main.allertview.UIAllertActionLast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UIAlertAction : BottomSheetDialogFragment() {

    private val BUTTON = 0
    private val EDIT_TEXT = 1

    private class Action(
        var type: Int,
        var text: String,
        var block: (Any?) -> Unit
    )

    private var actions: Array<Action> = emptyArray()
    private var title: String? = null
    private var cancelText: String? = null
    private var cancelAction: () -> Unit = { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val divider = ShapeDrawable(RectShape())
        divider.paint.color = Color.LTGRAY
        divider.intrinsicHeight = 5

        val linearLayout = LinearLayout(requireContext())
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.background = GradientDrawable()
        (linearLayout.background as GradientDrawable).cornerRadius = 16F
        (linearLayout.background as GradientDrawable).setColor(Color.WHITE)
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = divider

        if(title != null) {
            val textView = TextView(requireContext())
            textView.text = title
            textView.gravity = Gravity.CENTER
            textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150)
            linearLayout.addView(textView)
        }

        //MARK("Инициализация кнопок и полей ввода.")
        for(action in actions) {
            if (action.type == BUTTON) {
//                linearLayout.addViewFX("Кнопка", TextView(requireContext())
//                    .size(ViewGroup.LayoutParams.MATCH_PARENT, 60)
//                    .color(Color.BLACK)
//                    .text(action.text)
//                    .click {
//                        action.block(it)
//                        dismiss()
//                    }
//                )
                val actionView = TextView(requireContext())
                actionView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150)
                actionView.setTextColor(Color.BLACK)
                actionView.text = action.text
                actionView.gravity = Gravity.CENTER
                actionView.setOnClickListener {
                    action.block(null)
                    dismiss()
                }
                linearLayout.addView(actionView)

            } else if (action.type == EDIT_TEXT) {
//                linearLayout.addViewFX("Поле ввода", EditText(requireContext())
//                    .input(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT)
//                    .size(ViewGroup.LayoutParams.MATCH_PARENT, 60)
//                    .color(Color.BLACK)
//                    .text(action.text)
//                    .changed { action.block(it) }
//                )
                val actionView = EditText(requireContext())
                actionView.setText(action.text)
                actionView.inputType = InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT
                actionView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150)
                actionView.setTextColor(Color.BLACK)
                actionView.gravity = Gravity.CENTER
                actionView.addTextChangedListener{
                    action.block(it)
                }
                linearLayout.addView(actionView)
            }
        }

        //MARK("Инициализация разделительного элемента.")
        val separatorView = TextView(requireContext())
        separatorView.height = 12

        //MARK("Инициализация кнопки отмены.")
        val cancelView = TextView(requireContext())
        cancelView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150)
        cancelView.background = GradientDrawable()
        (cancelView.background as GradientDrawable).cornerRadius = 16F
        (cancelView.background as GradientDrawable).setColor(Color.WHITE)
        cancelView.setTextColor(Color.BLACK)
        cancelView.text = cancelText ?: "Отмена"
        cancelView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        cancelView.gravity = Gravity.CENTER
        cancelView.setOnClickListener { cancelAction(); dismiss()}

        //MARK("Инициализация родительского контейнера.")
        val parentLinearLayout = LinearLayout(requireContext())
        parentLinearLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parentLinearLayout.gravity = Gravity.CENTER_HORIZONTAL
        parentLinearLayout.orientation = LinearLayout.VERTICAL
        parentLinearLayout.setPadding(20)
        parentLinearLayout.setBackgroundColor(Color.TRANSPARENT)
        parentLinearLayout.addView(linearLayout)
        parentLinearLayout.addView(separatorView)
        parentLinearLayout.addView(cancelView)

        return parentLinearLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setOnShowListener {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    fun title(text: String): UIAlertAction {
        this.title = text
        return this
    }

    fun edit(text: String, block: (Any?) -> Unit): UIAlertAction {
        this.actions += Action(EDIT_TEXT, text, block)
        return this
    }

    fun button(text: String, block: (Any?) -> Unit): UIAlertAction {
        this.actions += Action(BUTTON, text, block)
        return this
    }

    fun cancel(text: String, block: () -> Unit = { }): UIAlertAction {
        this.cancelText = text
        this.cancelAction = block
        return this
    }

    fun show() { Companion.activity?.let { super.show(it.supportFragmentManager, TAG) }}

    companion object {
        private var activity: AppCompatActivity? = null
        fun connectTo(activity: AppCompatActivity){
            this.activity = activity
        }
        const val TAG = "UIAllertAction"
    }

}