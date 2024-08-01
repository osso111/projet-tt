/*package com.example.elapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentResultsFragment : Fragment() {

    private lateinit var viewModel: BillViewModel
    private lateinit var adapter: BillAdapter

    private val TAG = "PaymentFragmentResults"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.`fragment_payment_details.xml`, container, false)

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BillAdapter { bill -> onBillClick(bill) }
        recyclerView.adapter = adapter

        // Retrieve bills data
        viewModel = ViewModelProvider(requireActivity()).get(BillViewModel::class.java)
        viewModel.bills.observe(viewLifecycleOwner, Observer { bills ->
            adapter.submitList(bills)
        })

        return view
    }

    private fun onBillClick(bill:Facture) {
        // Show button on bill item click
        val payButton =
            payButton.visibility = View.VISIBLE
        payButton.setOnClickListener {
            showPaymentConfirmationDialog(bill)
        }
    }

    private fun showPaymentConfirmationDialog(bill:Facture) {
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
                        RetrofitInstance.instance.updateFacture(bill.numFacture, currentDate)
                        // Refresh the bill list if necessary
                    } catch (e: Exception) {
                        Log.e(TAG, "Error updating bill status", e)
                    }
                }
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
*/