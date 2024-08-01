package com.example.elapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PaymentFragment", "onViewCreated called")

        viewModel = PaymentViewModel() // Initialize the ViewModel

        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.phone_number_edit_text)
        val button = view.findViewById<Button>(R.id.validate_button)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)
            phoneNumberEditText.hint = radioButton.text
            Log.d("PaymentFragment", "Radio button checked: ${radioButton.text}")
        }

        button.setOnClickListener {
            val query = phoneNumberEditText.text.toString()
            Log.d("PaymentFragment", "Button clicked with query: $query")
            if (query.isNotBlank()) {
                fetchFactures(query)
            }
        }

        viewModel.factures.observe(viewLifecycleOwner) { factures ->
            Log.d("PaymentFragment", "Factures observed: $factures")

            // Create a new instance of the BillDetailsFragment and pass the factures data
            val billDetailsFragment = BillDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("factures", ArrayList(factures))
                    putString("query", phoneNumberEditText.text.toString())
                }
            }

            // Replace the current fragment with the BillDetailsFragment
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, billDetailsFragment)
                addToBackStack(null) // Add to back stack to allow return to PaymentFragment
                commit()
            }
        }
    }

    private fun fetchFactures(query: String) {
        Log.d("PaymentFragment", "fetchFactures called with query: $query")
        lifecycleScope.launch {
            try {
                val isPhoneNumber = query.length == 8
                val bills = if (isPhoneNumber) {
                    Log.d("PaymentFragment", "Fetching bills by phone number")
                    RetrofitInstance.instance.getUnpaidBills(phoneNumber = query)
                } else {
                    Log.d("PaymentFragment", "Fetching bills by bill number")
                    RetrofitInstance.instance.getUnpaidBills(billNumber = query)
                }

                // Log the full response
                Log.d("PaymentFragment", "Response: $bills")

                viewModel.setFactures(bills)

                Log.d("PaymentFragment", "fetchFactures completed successfully")

            } catch (e: Exception) {
                Log.e("PaymentFragment", "Error fetching bills", e)
            }
        }
    }
}
