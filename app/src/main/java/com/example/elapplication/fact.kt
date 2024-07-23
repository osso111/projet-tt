// Facture.kt
package com.example.elapplication

data class fact(
    val num_facture: String,
    val num_tel: String,
    val date_creation: String,
    val date_payment: String?,
    val delai_fin_payment: String,
    val etat: String,
    val type_fact: String,
    val montant: Double
)