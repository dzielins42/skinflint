package pl.dzielins42.skinflint.view.transactions.list

import android.text.format.DateFormat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_transaction.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.data.entity.Transaction
import java.text.NumberFormat
import java.util.*

class TransactionItem(
    val transaction: Transaction
) : AbstractFlexibleItem<TransactionFlexibleViewHolder>() {

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: TransactionFlexibleViewHolder,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder.bind(transaction)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is TransactionItem) {
            other.transaction.id == this.transaction.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return transaction.hashCode()
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): TransactionFlexibleViewHolder {
        return TransactionFlexibleViewHolder(view, adapter)
    }

    override fun getLayoutRes(): Int = R.layout.item_transaction
}

class TransactionFlexibleViewHolder(
    view: View, adapter: FlexibleAdapter<out IFlexible<*>>
) : FlexibleViewHolder(view, adapter), LayoutContainer {

    override val containerView: View?
        get() = itemView

    fun bind(item: Transaction) {
        mainText.text = item.name
        secondaryText.text =
            NumberFormat.getCurrencyInstance().apply {
                currency = Currency.getInstance("PLN")
            }.format(item.value / 100.0)
        metaText.text = DateFormat.getDateFormat(itemView.context).format(item.date)
    }

    override fun onClick(view: View) {
        super.onClick(view)
    }
}