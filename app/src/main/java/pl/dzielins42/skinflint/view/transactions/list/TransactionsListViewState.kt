package pl.dzielins42.skinflint.view.transactions.list

import pl.dzielins42.skinflint.data.entity.Transaction

data class TransactionsListViewState(
    val transactions: List<Transaction>
)