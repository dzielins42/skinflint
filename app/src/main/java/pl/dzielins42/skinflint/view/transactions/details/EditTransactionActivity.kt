package pl.dzielins42.skinflint.view.transactions.details

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_edit_transaction.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.business.InputFormField
import pl.dzielins42.skinflint.business.TransactionInputForm
import pl.dzielins42.skinflint.data.entity.Transaction
import pl.dzielins42.skinflint.ext.getDoubleInput
import pl.dzielins42.skinflint.ext.getInput
import java.util.*
import javax.inject.Inject

class EditTransactionActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: EditTransactionViewModel

    private var editedTransactionId: Long = 0

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_edit_transaction)
        setSupportActionBar(toolbar)

        editedTransactionId = intent.getLongExtra(EXTRA_EDITED_TRANSACTION_ID, 0L)

        date.setOnClickListener { showDatePicker() }
        time.setOnClickListener { showTimePicker() }

        if (savedInstanceState == null) {
            // Call only first time it's created
            viewModel.setEditedTransaction(editedTransactionId)
        }

        viewModel.viewState.observe(
            this,
            Observer<EditTransactionViewState> { viewState ->
                applyViewState(viewState)
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_transaction_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {
                viewModel.saveTransaction(composeTransactionInputForm())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyViewState(viewState: EditTransactionViewState) {
        if (viewState.finish) {
            finish()
        }

        editedTransactionId = viewState.form.id

        val inputForm = viewState.form

        name.setText(inputForm.name.value)
        value.setText(inputForm.value.value.toString())
        description.setText(inputForm.description.value)
        calendar.time = inputForm.date.value.time
        setDateField(calendar)
        setTimeField(calendar)

        nameInputLayout.error = when (inputForm.name.error) {
            InputFormField.ERROR_EMPTY -> R.string.form_error_empty
            else -> null
        }?.let { getString(it) }
        valueInputLayout.error = when (inputForm.value.error) {
            TransactionInputForm.ERROR_INVALID_VALUE -> R.string.form_error_invalid_value
            else -> null
        }?.let { getString(it) }

        title = if (editedTransactionId > 0) inputForm.name.value else getString(R.string.transaction_new)
    }

    private fun composeTransactionInputForm(): TransactionInputForm {
        return TransactionInputForm(
            editedTransactionId,
            name.getInput(),
            InputFormField("$"),
            description.getInput(),
            InputFormField(calendar),
            value.getDoubleInput()
        )
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            0,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                setDateField(calendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                })
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            0,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                setTimeField(calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                })
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(this)
        ).show()
    }

    private fun setDateField(calendar: Calendar) {
        date.setText(DateFormat.getDateFormat(this).format(calendar.time))
    }

    private fun setTimeField(calendar: Calendar) {
        time.setText(DateFormat.getTimeFormat(this).format(calendar.time))
    }

    companion object {
        private const val EXTRA_EDITED_TRANSACTION_ID =
            "pl.dzielins42.skinflint.view.transactions.details.EditTransactionActivity.EDITED_TRANSACTION_ID"

        fun getIntent(context: Context, editedTransactionId: Long? = 0): Intent {
            return Intent(context, EditTransactionActivity::class.java)
                .putExtra(EXTRA_EDITED_TRANSACTION_ID, editedTransactionId)
        }
    }
}
