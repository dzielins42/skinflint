package pl.dzielins42.skinflint.view

import pl.dzielins42.skinflint.data.entity.Transaction

data class TransactionsListViewState(
    val transactions: List<Transaction>
)