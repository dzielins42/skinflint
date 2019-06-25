package pl.dzielins42.skinflint.ext

import android.widget.EditText
import pl.dzielins42.skinflint.business.InputFormField

fun EditText.getInput(): InputFormField<String> {
    return InputFormField(this.text.toString())
}

fun EditText.getStringInput(): InputFormField<String> {
    return this.getInput()
}

fun EditText.getDoubleInput(): InputFormField<Double> {
    return InputFormField(this.text.toString().toDoubleOrNull() ?: Double.NaN)
}