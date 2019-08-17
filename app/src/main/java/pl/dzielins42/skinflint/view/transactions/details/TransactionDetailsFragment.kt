package pl.dzielins42.skinflint.view.transactions.details

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_transaction_details.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.business.InputFormField
import pl.dzielins42.skinflint.business.TransactionInputForm
import pl.dzielins42.skinflint.util.ext.getDoubleInput
import pl.dzielins42.skinflint.util.ext.getInput
import java.util.*
import javax.inject.Inject

class TransactionDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModel: EditTransactionViewModel

    private val args: TransactionDetailsFragmentArgs by navArgs()
    private var transactionId: Long = 0
    private val calendar: Calendar = Calendar.getInstance()

    // TODO Check if you can fix it better
    // This is used because on configuration change, when subscribing to viewModel.viewState, Observer is triggered
    // AFTER savedInstanceState is used to restore state of the View, so values are overridden by those from ViewModel.
    // This is undesired because while ViewModel provides persisted data from Repository, View holds volatile draft
    // that should not be discarded.
    // Alternative solution: save volatile draft in ViewModel (but not in Repository).
    private var skipFirstViewState = false

    //region Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NavigationUI.setupWithNavController(toolbar, findNavController())

        transactionId = args.transactionId

        setupUi()

        if (savedInstanceState == null) {
            // Call only first time it's created
            viewModel.setEditedTransaction(transactionId)
        } else {
            skipFirstViewState = true
        }

        viewModel.viewState.observe(
            viewLifecycleOwner,
            Observer<EditTransactionViewState> { viewState ->
                applyViewState(viewState)
            }
        )
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
    //endregion

    private fun setupUi() {
        date.setOnClickListener { showDatePicker() }
        time.setOnClickListener { showTimePicker() }

        toolbar.inflateMenu(R.menu.menu_transaction_details)
        toolbar.menu.findItem(R.id.menu_delete).isVisible = transactionId > 0
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_done -> {
                    viewModel.saveTransaction(composeTransactionInputForm())
                    true
                }
                R.id.menu_delete -> {
                    deleteTransaction()
                    true
                }
                else -> false
            }
        }
    }

    private fun applyViewState(viewState: EditTransactionViewState) {
        if (skipFirstViewState) {
            skipFirstViewState = false
            return
        }

        if (viewState.finish) {
            findNavController().popBackStack()
        }

        transactionId = viewState.form.id

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

        toolbar.title = if (transactionId > 0) inputForm.name.value else getString(R.string.transaction_new)
    }

    private fun composeTransactionInputForm(): TransactionInputForm {
        return TransactionInputForm(
            transactionId,
            name.getInput(),
            InputFormField("$"),
            description.getInput(),
            InputFormField(calendar),
            value.getDoubleInput()
        )
    }

    private fun deleteTransaction() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.confirm_transaction_delete_title)
            .setMessage(R.string.confirm_transaction_delete_msg)
            .setPositiveButton(R.string.ok) { _, _ -> viewModel.deleteTransaction(transactionId) }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            context!!,
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
            context!!,
            0,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                setTimeField(calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                })
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun setDateField(calendar: Calendar) {
        date.setText(DateFormat.getDateFormat(context).format(calendar.time))
    }

    private fun setTimeField(calendar: Calendar) {
        time.setText(DateFormat.getTimeFormat(context).format(calendar.time))
    }
}
