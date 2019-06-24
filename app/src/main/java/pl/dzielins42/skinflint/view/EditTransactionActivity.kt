package pl.dzielins42.skinflint.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_transaction.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.data.entity.Transaction
import java.util.*

class EditTransactionActivity : AppCompatActivity() {

    private var editedTransaction: Transaction? = null

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)
        setSupportActionBar(toolbar)

        date.setOnClickListener {
            showDatePicker()
        }

        time.setOnClickListener {
            showTimePicker()
        }

        editedTransaction = intent.getParcelableExtra(EXTRA_EDITED_TRANSACTION)

        if (editedTransaction != null) {
            calendar.time = editedTransaction!!.date
        }

        setDateField(calendar)
        setTimeField(calendar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_transaction_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_done -> true
            else -> super.onOptionsItemSelected(item)
        }
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
        private const val EXTRA_EDITED_TRANSACTION =
            "pl.dzielins42.skinflint.view.EditTransactionActivity.EDITED_TRANSACTION"

        fun getIntent(context: Context, editedTransaction: Transaction? = null): Intent {
            return Intent(context, EditTransactionActivity::class.java)
                .putExtra(EXTRA_EDITED_TRANSACTION, editedTransaction)
        }
    }
}
