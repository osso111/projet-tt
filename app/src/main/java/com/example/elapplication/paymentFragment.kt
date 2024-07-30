package com.example.elapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SetTextI18n")
class PaymentFragment : Fragment() {

    private val TAG = "PaymentFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: called")
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: called")

        // Initialize views
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
        val phoneNumberTextView = view.findViewById<TextView>(R.id.phone_number_text_view)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.phone_number_edit_text)
        val button = view.findViewById<Button>(R.id.validate_button)
        val billsContainer = view.findViewById<LinearLayout>(R.id.bills_container)

        Log.d(TAG, "onViewCreated: views initialized")

        // Set listener on RadioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)
            val selectedText = radioButton.text.toString()

            // Update the TextView and EditText based on the selected RadioButton
            phoneNumberTextView.text = "$selectedText * :"
            phoneNumberEditText.hint = selectedText

            Log.d(TAG, "onCheckedChangeListener: selectedText = $selectedText")
        }

        // Set listener on Button
        button.setOnClickListener {
            val query = phoneNumberEditText.text.toString()
            Log.d(TAG, "validate_button onClick: query = $query")
            if (query.isNotBlank()) {
                fetchFactures(query, billsContainer)
            } else {
                Log.d(TAG, "validate_button onClick: query is blank")
            }
        }
    }

    private fun fetchFactures(query: String, billsContainer: LinearLayout) {
        Log.d(TAG, "fetchFactures: called with query = $query")
        val container = billsContainer
        lifecycleScope.launch {
            try {
                val isPhoneNumber = query.length == 8
                val bills = if (isPhoneNumber) {
                    Log.d(TAG, "fetchFactures: querying by phone number")
                    RetrofitInstance.instance.getUnpaidBills(phoneNumber = query)
                } else {
                    Log.d(TAG, "fetchFactures: querying by bill number")
                    RetrofitInstance.instance.getUnpaidBills(billNumber = query)
                }

                // Log the full response
                Log.d(TAG, "fetchFactures: response = $bills")

                // Update the UI with the fetched factures
                updateFactures(container, bills)

                Log.d(TAG, "fetchFactures: completed successfully")

            } catch (e: Exception) {
                Log.e(TAG, "fetchFactures: error occurred", e)
            }
        }
    }



    private fun updateFactures(container: LinearLayout, factures: List<Facture>) {
        container.removeAllViews()
        val inflater = LayoutInflater.from(context)
         fun formatDateString(dateString: String?): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date: Date? = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }
        factures.forEach { facture ->
            Log.d(TAG, "payment: retrieved facture = $facture")
            val billView = TextView(requireContext()).apply {
                text = """
                        Numéro Facture: ${facture.numFacture}
                        Numéro Téléphone: ${facture.numTel}
                        Date Création: ${formatDateString(facture.dateCreation)}
                        Date Payment: ${formatDateString(facture.datePayment)}
                        Délai Fin Payment: ${formatDateString(facture.delaiFinPayment)}
                        État: ${facture.etat}
                        Type Fact: ${facture.typeFact}
                        Montant: ${facture.montant}
                    """.trimIndent()
                setPadding(30, 20, 16, 50)
                setBackgroundColor(resources.getColor(R.color.white, null)) // Set background color to white
                setBackgroundResource(R.drawable.corner_round)
                setTextColor(resources.getColor(R.color.black, null))
                textSize = 16f
                setOnClickListener {
                    Log.d(TAG, "payment: clicked on facture = $facture")
                    showConfirmationDialog(facture)
                    }
                }
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20, 20, 20, 20) // Left, top, right, bottom margins
            }
            billView.layoutParams = layoutParams
            container.addView(billView)
        }
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun showConfirmationDialog(facture: Facture) {
        val currentDate = getCurrentDate()
        // Create and show a confirmation dialog
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to pay this bill?")
            .setPositiveButton("Yes") { _, _ ->
                // Handle the confirmation action
                lifecycleScope.launch {
                    try {
                        // Call the API to update the bill status to 'paid'
                        RetrofitInstance.instance.updateFacture(facture.numFacture, currentDate)
                        Log.d(TAG, "numfact=  ${facture.numFacture}")
                        // Refresh the bill list
                        fetchFactures(facture.numTel ?: "", view?.findViewById(R.id.bills_container) ?: return@launch)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error updating bill status", e)
                    }
                }
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }

}
