package com.example.elapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentViewModel : ViewModel() {

    private val _factures = MutableLiveData<List<Facture>>()
    val factures: LiveData<List<Facture>> get() = _factures

    fun setFactures(factures: List<Facture>) {
        _factures.value = factures
    }
}
