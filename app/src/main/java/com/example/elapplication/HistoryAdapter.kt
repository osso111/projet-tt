package com.example.elapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HistoryAdapter(private val factureList: List<Facture>) : RecyclerView.Adapter<HistoryAdapter.FactureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_facture, parent, false)
        return FactureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FactureViewHolder, position: Int) {
        val facture = factureList[position]
        holder.bind(facture)
    }

    override fun getItemCount(): Int = factureList.size




    class FactureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBillNumber: TextView = itemView.findViewById(R.id.textViewNumFacture)
        private val tvPhoneNumber: TextView = itemView.findViewById(R.id.textViewNumTel)
        private val tvCreationDate: TextView = itemView.findViewById(R.id.textViewDateCreation)
        private val tvPaymentDate: TextView = itemView.findViewById(R.id.textViewDatePayment)
        private val tvDueDate: TextView = itemView.findViewById(R.id.textViewDelaiFinPayment)
        private val tvStatus: TextView = itemView.findViewById(R.id.textViewEtat)
        private val tvType: TextView = itemView.findViewById(R.id.textViewTypeFact)
        private val tvAmount: TextView = itemView.findViewById(R.id.textViewMontant)

        @SuppressLint("SetTextI18n")
        fun bind(facture: Facture) {
            tvBillNumber.text = "Bill Number: ${facture.numFacture}"
            tvPhoneNumber.text = "Phone Number: ${facture.numTel}"
            tvCreationDate.text = "Creation Date: ${formatDateString(facture.dateCreation)}"
            tvPaymentDate.text = "Payment Date: ${formatDateString(facture.datePayment)}"
            tvDueDate.text = "Due Date: ${formatDateString(facture.delaiFinPayment)}"
            tvStatus.text = "Status: ${facture.etat}"
            tvType.text = "Type: ${facture.typeFact}"
            tvAmount.text = "Amount: ${facture.montant}"
        }
        private fun formatDateString(dateString: String?): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date: Date? = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }

    }
}