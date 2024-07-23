package com.example.elapplication

import com.google.gson.annotations.SerializedName

data class Facture(
    @SerializedName("num_facture") val numFacture: String,
    @SerializedName("num_tel") val numTel: String,
    @SerializedName("date_creation") val dateCreation: String?,
    @SerializedName("date_payment") val datePayment: String?,
    @SerializedName("delai_fin_payment") val delaiFinPayment: String?,
    @SerializedName("etat") val etat: String?,
    @SerializedName("type_fact") val typeFact: String?,
    @SerializedName("montant") val montant: Double?
)

