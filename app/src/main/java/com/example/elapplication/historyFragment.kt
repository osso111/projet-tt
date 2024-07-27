package com.example.elapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var factureList: List<Facture>
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("HistoryFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.historyRecyclerView)
        Log.d("HistoryFragment", "RecyclerView initialized")
        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = HistoryAdapter(emptyList())
        recyclerView.adapter = historyAdapter
        Log.d("HistoryFragment", "HistoryAdapter initialized")
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val phoneNumber = sharedViewModel.phoneNumber

        Log.d("HistoryFragment", "Fetching history factures for phone number: $phoneNumber")
        fetchHistoryFactures(phoneNumber)

        return view
    }

    private fun fetchHistoryFactures(phoneNumber: String?) {
        Log.d("HistoryFragment", "fetchHistoryFactures called with phone number: $phoneNumber")
        val request = mapOf("phoneNumber" to phoneNumber.orEmpty())
        RetrofitInstance.instance.getFactureHistory(request).enqueue(object : Callback<List<Facture>> {
            override fun onResponse(call: Call<List<Facture>>, response: Response<List<Facture>>) {
                Log.d("HistoryFragment", "onResponse called")
                if (response.isSuccessful) {
                    Log.d("HistoryFragment", "Response successful")
                    factureList = response.body() ?: emptyList()
                    Log.d("HistoryFragment", "Fetched ${factureList.size} factures")
                    updateRecyclerView()
                } else {
                    Log.e("HistoryFragment", "Response not successful: ${response.code()} ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("HistoryFragment", "Error body: $errorBody")
                }
            }

            override fun onFailure(call: Call<List<Facture>>, t: Throwable) {
                Log.e("HistoryFragment", "onFailure called: ${t.message}")
            }
        })
    }


    private fun updateRecyclerView() {
        Log.d("HistoryFragment", "Updating RecyclerView with ${factureList.size} factures")
        historyAdapter = HistoryAdapter(factureList)
        recyclerView.adapter = historyAdapter
    }
}
