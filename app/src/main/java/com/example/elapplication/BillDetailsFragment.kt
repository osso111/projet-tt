package com.example.elapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.appcompat.app.AlertDialog

class BillDetailsFragment : Fragment() {

    private lateinit var selectedFactures: MutableList<Facture>
    private lateinit var factures: List<Facture>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bill_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedFactures = mutableListOf()

        // Retrieve the list of bills from the arguments bundle
        factures = arguments?.getParcelableArrayList("factures") ?: listOf()
        Log.d("BillDetailsFragment", "Factures: $factures")
        showResults()
    }

    private fun showResults() {
        val tableLayout = view?.findViewById<LinearLayout>(R.id.table_layout) ?: return
        tableLayout.removeAllViews()

        if (factures.isEmpty()) {
            // No bills to show
            val noBillsTextView = TextView(requireContext()).apply {
                text = "No unpaid bills found."
                setPadding(16, 16, 16, 16)
            }
            tableLayout.addView(noBillsTextView)
        } else {
            // Add header row
            val headerRow = createRow("Bill Number", "Amount", "Phone Number", true)
            tableLayout.addView(headerRow)

            // Add data rows with checkboxes
            factures.forEach { facture ->
                val dataRow = createRow(facture.numFacture, facture.montant.toString(), facture.numTel, false)
                tableLayout.addView(dataRow)

                val checkbox = CheckBox(requireContext()).apply {
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            selectedFactures.add(facture)
                        } else {
                            selectedFactures.remove(facture)
                        }
                        // Show or hide the pay button based on selection
                        view?.findViewById<Button>(R.id.pay_button)?.visibility =
                            if (selectedFactures.isNotEmpty()) View.VISIBLE else View.GONE
                    }
                }
                dataRow.addView(checkbox)
            }

            // Add Pay button at the bottom
            val payButton = Button(requireContext()).apply {
                text = "Pay"
                id = R.id.pay_button
                visibility = View.GONE
                setOnClickListener {
                    showPaymentConfirmationDialog()
                }
            }
            tableLayout.addView(payButton)
        }
    }

    private fun createRow(col1: String, col2: String, col3: String, isHeader: Boolean): LinearLayout {
        val row = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val params = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        listOf(col1, col2, col3).forEach { text ->
            val textView = TextView(requireContext()).apply {
                layoutParams = params
                this.text = text
                setPadding(8, 8, 8, 8)
                if (isHeader) {
                    setTypeface(null, android.graphics.Typeface.BOLD)
                }
            }
            row.addView(textView)
        }

        return row
    }

    private fun showPaymentConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Payment Confirmation")
            .setMessage("Are you sure you want to pay for the selected bills?")
            .setPositiveButton("Yes") { _, _ ->
                processPayment()
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }

    private fun processPayment() {
        lifecycleScope.launch {
            try {
                val initialCount = factures.size
                selectedFactures.forEach { facture ->
                    // Call the API to update the bill status to 'paid'
                    val currentDate = getCurrentDate()
                    RetrofitInstance.instance.updateFacture(facture.numFacture, currentDate)
                }
                // Refresh results if not all bills are paid
                if (factures.size > initialCount - selectedFactures.size) {
                    refreshResults()
                } else {
                    showPaymentSuccessDialog()
                }
            } catch (e: Exception) {
                showPaymentErrorDialog()
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun showPaymentSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Payment Successful")
            .setMessage("The selected bills have been paid successfully.")
            .setPositiveButton("OK") { _, _ -> refreshResults() }
            .show()
    }

    private fun showPaymentErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Payment Error")
            .setMessage("There was an error processing the payment. Please try again.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun refreshResults() {
        // Refresh the list of unpaid bills
        val query = arguments?.getString("query") ?: return
        lifecycleScope.launch {
            try {
                val bills = RetrofitInstance.instance.getUnpaidBills(query)
                if (bills.isEmpty()) {
                    // Handle case where no unpaid bills are found after payment
                    view?.findViewById<LinearLayout>(R.id.table_layout)?.apply {
                        removeAllViews()
                        val noBillsTextView = TextView(requireContext()).apply {
                            text = "No unpaid bills found."
                            setPadding(16, 16, 16, 16)
                        }
                        addView(noBillsTextView)
                    }
                } else {
                    // Update the bill list and UI
                    factures = bills
                    showResults()
                }
            } catch (e: Exception) {
                Log.e("BillDetailsFragment", "Error refreshing bills", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("BillDetailsFragment", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("BillDetailsFragment", "onPause called")
    }
}
