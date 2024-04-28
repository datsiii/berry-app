package com.example.berryapp

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Base64
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.yandex.runtime.Runtime
import java.io.ByteArrayOutputStream
import java.io.IOException

class DevDialog : DialogFragment() {
    companion object {
        private const val TAG = "DialogPMarkFragment"
        private const val EXTRA_TITLE = "Добавить устройство"
        private const val EXTRA_HINT = "Mетка"
        private const val EXTRA_MULTILINE = "multiline"
        private const val EXTRA_TEXT = "text"

        fun newInstance(
            title: String? = null,
            hint: String? = null,
            text: String? = null,
            isMultiline: Boolean = false
        ): DevDialog {
            val dialog = DevDialog()
            val args = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_HINT, hint)
                putString(EXTRA_TEXT, text)
                putBoolean(EXTRA_MULTILINE, isMultiline)
            }
            dialog.arguments = args
            return dialog
        }
    }

    lateinit var editText: TextInputEditText
    lateinit var editText2: TextInputEditText
    var onOk: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(EXTRA_TITLE)
        val hint = arguments?.getString(EXTRA_HINT)
        val text: String? = arguments?.getString(EXTRA_TEXT)
        val isMiltiline = arguments?.getBoolean(EXTRA_MULTILINE) ?: false
        val view = requireActivity().layoutInflater.inflate(R.layout.fragment_dev_dialog, null)
        editText = view.findViewById(R.id.editText)
        editText.hint = hint
        editText2 = view.findViewById(R.id.editText2)
        editText2.hint = hint

        /*var activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback<ActivityResult>() {
                fun onActivityResult(result: ActivityResult) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        val data: Intent? = result.getData()
                        val uri = data?.data
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity()?.getContentResolver(), uri)
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                }
            })*/

        if (isMiltiline) {
            editText.minLines = 3
            editText.inputType =
                InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }
        if (text != null) {
            // editText.setText(text)
            // editText.setSelection(text.length)
            editText.append(text)
        }
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(view)
            .setPositiveButton("Добавить") { _, _ ->
                onOk?.invoke()
            }
            .setNegativeButton("Отмена") { _, _ ->
                onCancel?.invoke()
            }
        val dialog = builder.create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }


}