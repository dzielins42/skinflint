package pl.dzielins42.skinflint.view.transactions.details

import pl.dzielins42.skinflint.business.TransactionInputForm

data class TransactionDetailsViewState(
    val form: TransactionInputForm,
    val finish: Boolean = false
)