package kg.bakai.notes.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kg.bakai.notes.R

class CustomDialog(
    private val title: String,
    private val cancel: String = "Нет",
    private val ok: String = "Сохранить",
    private val onSaveClick: Callback
): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_layout, view?.findViewById(R.id.dialog_layout), false)
        builder.setView(view)
        val alertDialog = builder.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        view?.apply {
            findViewById<TextView>(R.id.tv_dialog_title).apply { text = title }
            findViewById<Button>(R.id.btn_cancel).apply {
                text = cancel
                setOnClickListener {
                    this@CustomDialog.dismiss()
                }
            }
            findViewById<Button>(R.id.btn_ok).apply {
                text = ok
                setOnClickListener {
                    onSaveClick.onSaveClick()
                    this@CustomDialog.dismiss()
                }
            }
        }
        return alertDialog
    }

    interface Callback {
        fun onSaveClick()
    }
}