package com.example.elapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BillAdapter(
    private val onBillClick: (Facture) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    private var bills: List<Facture> = emptyList()

    fun submitList(newBills: List<Facture>) {
        bills = newBills
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bill_header, parent, false)
            HeaderViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bill_row, parent, false)
            BillViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BillViewHolder) {
            val bill = bills[position - 1] // Adjust for header
            holder.bind(bill)
        }
    }

    override fun getItemCount() = bills.size + 1 // +1 for header

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val billNumberText: TextView = itemView.findViewById(R.id.bill_number_text)
        private val billAmountText: TextView = itemView.findViewById(R.id.bill_amount_text)
        private val phoneNumberText: TextView = itemView.findViewById(R.id.phone_number_text)

        fun bind(bill: Facture) {
            billNumberText.text = bill.numFacture
            billAmountText.text = bill.montant.toString()
            phoneNumberText.text = bill.numTel

            itemView.setOnClickListener {
                itemView.findViewById<Button>(R.id.pay_button).visibility = View.VISIBLE
                itemView.findViewById<Button>(R.id.pay_button).setOnClickListener {
                    onBillClick(bill)
                }
            }
        }
    }
}
