package com.example.elapplication

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(numFacture)
        parcel.writeString(numTel)
        parcel.writeString(dateCreation)
        parcel.writeString(datePayment)
        parcel.writeString(delaiFinPayment)
        parcel.writeString(etat)
        parcel.writeString(typeFact)
        parcel.writeValue(montant)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Facture> {
        override fun createFromParcel(parcel: Parcel): Facture {
            return Facture(parcel)
        }

        override fun newArray(size: Int): Array<Facture?> {
            return arrayOfNulls(size)
        }
    }
}
