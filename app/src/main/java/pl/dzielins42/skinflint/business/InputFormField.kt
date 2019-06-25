package pl.dzielins42.skinflint.business

data class InputFormField<T>(
    val value: T,
    val error: Int = ERROR_NONE
) {
    fun isValid(): Boolean = error == ERROR_NONE

    fun validate(validator: (T) -> Int): InputFormField<T> {
        return copy(error = validator.invoke(value))
    }

    companion object {
        const val ERROR_NONE = -1
        const val ERROR_EMPTY = 1
    }
}