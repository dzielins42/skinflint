package pl.dzielins42.skinflint.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_transaction.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.data.entity.Transaction

class TransactionsAdapter : ListAdapter<Transaction, TransactionViewHolder>(TransactionsDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder, position: Int
    ) {
        holder.bind(getItem(position))
    }
}

class TransactionViewHolder(
    view: View
) : RecyclerView.ViewHolder(view), LayoutContainer {
    override val containerView: View?
        get() = itemView

    fun bind(item: Transaction) {
        text.text = item.name
    }
}

class TransactionsDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(
        oldItem: Transaction, newItem: Transaction
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Transaction, newItem: Transaction
    ): Boolean {
        return oldItem == newItem
    }

}