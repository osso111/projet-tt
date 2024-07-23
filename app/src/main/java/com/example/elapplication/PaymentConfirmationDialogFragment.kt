package com.example.elapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PaymentConfirmationDialogFragment(
    private val facture: Facture,
    private val onConfirm: (Facture?) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to pay this bill?")
            .setPositiveButton("Yes") { dialog, which ->
                onConfirm(facture)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which ->
                onConfirm(null)
                dialog.dismiss()
            }
            .create()
    }
}
