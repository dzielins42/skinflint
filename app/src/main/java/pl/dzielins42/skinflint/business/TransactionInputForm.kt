package pl.dzielins42.skinflint.business

import java.util.*

data class TransactionInputForm(
    val id: Long = 0,
    val name: InputFormField<String>,
    val currency: InputFormField<String>,
    val description: InputFormField<String>,
    val date: InputFormField<Calendar>,
    val value: InputFormField<Double>
) {
    fun isValid(): Boolean {
        return name.isValid()
                && currency.isValid()
                && description.isValid()
                && date.isValid()
                && value.isValid()
    }

    companion object {
        const val ERROR_INVALID_VALUE = 101
    }
}