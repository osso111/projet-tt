/*package com.example.elapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FactureAdapter(
    private val factures: List<Facture>,
    private val onConfirmPayment: (Facture) -> Unit
) : RecyclerView.Adapter<FactureAdapter.FactureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bill_item, parent, false)
        return FactureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FactureViewHolder, position: Int) {
        val facture = factures[position]
        holder.bind(facture)
        holder.itemView.setOnClickListener {
            onConfirmPayment(facture)
        }
    }

    override fun getItemCount(): Int = factures.size

    class FactureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numFactureTextView: TextView = itemView.findViewById(R.id.num_facture_text_view)
        private val numTelTextView: TextView = itemView.findViewById(R.id.num_tel_text_view)
        private val dateCreationTextView: TextView = itemView.findViewById(R.id.date_creation_text_view)
        private val etatTextView: TextView = itemView.findViewById(R.id.etat_text_view)
        private val typeFactTextView: TextView = itemView.findViewById(R.id.type_fact_text_view)
        private val montantTextView: TextView = itemView.findViewById(R.id.montant_text_view)

        fun bind(facture: Facture, onItemClick: (Facture) -> Unit) {
            numFactureTextView.text = facture.numFacture
            numTelTextView.text = facture.numTel
            dateCreationTextView.text = facture.dateCreation
            etatTextView.text = facture.etat
            typeFactTextView.text = facture.typeFact
            montantTextView.text = facture.montant.toString()
            itemView.setOnClickListener { onItemClick(facture) }
        }
    }
}*/
