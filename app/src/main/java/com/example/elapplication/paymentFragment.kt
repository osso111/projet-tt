// PaymentFragment.kt
package com.example.elapplication

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

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
                payment(query, billsContainer)
            } else {
                Log.d(TAG, "validate_button onClick: query is blank")
            }
        }
    }

    private fun payment(query: String, billsContainer: LinearLayout) {
        Log.d(TAG, "payment: called with query = $query")

        lifecycleScope.launch {
            try {
                // Determine if the query is a phone number or bill number
                val isPhoneNumber = query.length == 8
                val bills = if (isPhoneNumber) {
                    Log.d(TAG, "payment: querying by phone number")
                    RetrofitInstance.instance.getUnpaidBills("unpaid", phoneNumber = query)
                } else {
                    Log.d(TAG, "payment: querying by bill number")
                    RetrofitInstance.instance.getUnpaidBills("unpaid", billNumber = query)
                }

                // Clear the previous results
                billsContainer.removeAllViews()

                // Log the bills retrieved and display in the container
                bills.forEach { facture ->
                    Log.d(TAG, "payment: retrieved facture = $facture")

                    val billView = TextView(requireContext()).apply {
                        text = """
                            Numéro Facture: ${facture.num_facture}
                            Numéro Téléphone: ${facture.num_tel}
                            Date Création: ${facture.date_creation}
                            Date Payment: ${facture.date_payment ?: "N/A"}
                            Délai Fin Payment: ${facture.delai_fin_payment}
                            État: ${facture.etat}
                            Type Fact: ${facture.type_fact}
                            Montant: ${facture.montant}
                        """.trimIndent()
                    }
                    billsContainer.addView(billView)
                }

                // Handle the result (display in UI or log)
                Log.d(TAG, "payment: completed successfully")

            } catch (e: Exception) {
                Log.e(TAG, "payment: error occurred", e)
            }
        }
    }
}
