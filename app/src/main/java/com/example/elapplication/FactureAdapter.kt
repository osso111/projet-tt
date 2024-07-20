package com.example.elapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FactureAdapter(private val factures: List<Facture>) : RecyclerView.Adapter<FactureAdapter.FactureViewHolder>() {

    class FactureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numFactureTextView: TextView = itemView.findViewById(R.id.num_facture)
        val numTelTextView: TextView = itemView.findViewById(R.id.num_tel)
        val dateCreationTextView: TextView = itemView.findViewById(R.id.date_creation)
        val datePaymentTextView: TextView = itemView.findViewById(R.id.date_payment)
        val delaiFinPaymentTextView: TextView = itemView.findViewById(R.id.delai_fin_payment)
        val etatTextView: TextView = itemView.findViewById(R.id.etat)
        val typeFactTextView: TextView = itemView.findViewById(R.id.type_fact)
        val montantTextView: TextView = itemView.findViewById(R.id.montant)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_facture, parent, false)
        return FactureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FactureViewHolder, position: Int) {
        val facture = factures[position]
        holder.numFactureTextView.text = facture.num_facture
        holder.numTelTextView.text = facture.num_tel
        holder.dateCreationTextView.text = facture.date_creation
        holder.datePaymentTextView.text = facture.date_payment ?: "N/A"
        holder.delaiFinPaymentTextView.text = facture.delai_fin_payment
        holder.etatTextView.text = facture.etat
        holder.typeFactTextView.text = facture.type_fact
        holder.montantTextView.text = facture.montant.toString()
    }

    override fun getItemCount(): Int {
        return factures.size
    }
}
