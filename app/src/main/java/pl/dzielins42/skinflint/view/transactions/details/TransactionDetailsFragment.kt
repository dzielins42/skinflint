package pl.dzielins42.skinflint.view.transactions.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import dagger.android.support.AndroidSupportInjection
import eltos.simpledialogfragment.SimpleDateDialog
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.SimpleTimeDialog
import kotlinx.android.synthetic.main.fragment_transaction_details.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.business.InputFormField
import pl.dzielins42.skinflint.business.TransactionInputForm
import pl.dzielins42.skinflint.util.ext.getDoubleInput
import pl.dzielins42.skinflint.util.ext.getInput
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject

class TransactionDetailsFragment : Fragment(), SimpleDialog.OnDialogResultListener {

    @Inject
    lateinit var viewModel: TransactionDetailsViewModel

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
            Observer<TransactionDetailsViewState> { viewState ->
                applyViewState(viewState)
            }
        )
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
    //endregion

    //region SimpleDialog.OnDialogResultListener
    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        if (which != SimpleDialog.OnDialogResultListener.BUTTON_POSITIVE) {
            return false
        }

        when (dialogTag) {
            TIME_PICKER_TAG -> {
                setTimeField(calendar.apply {
                    set(Calendar.HOUR_OF_DAY, extras.getInt(SimpleTimeDialog.HOUR))
                    set(Calendar.MINUTE, extras.getInt(SimpleTimeDialog.MINUTE))
                })
                return true
            }
            DATE_PICKER_TAG -> {
                setDateField(calendar.apply {
                    timeInMillis = extras.getLong(SimpleDateDialog.DATE)
                })
                return true
            }
            else -> return false
        }
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
                R.id.menu_share -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, composeTransactionToShare())
                        type = "text/plain"
                    }
                    context!!.startActivity(
                        Intent.createChooser(
                            sendIntent, resources.getString(R.string.transaction_share)
                        )
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun applyViewState(viewState: TransactionDetailsViewState) {
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
        value.setText(inputForm.value.value.let {
            if (it.isNaN()) null else it.toString()
        })
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

    private fun composeTransactionToShare(): String {
        val stringBuilder = StringBuilder()

        // TODO Make it better
        stringBuilder.append("Transaction ").append(name.text.toString())

        return stringBuilder.toString()
    }

    private fun deleteTransaction() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.confirm_transaction_delete_title)
            .setMessage(R.string.confirm_transaction_delete_msg)
            .setPositiveButton(R.string.ok) { _, _ -> viewModel.deleteTransaction(transactionId) }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    @SuppressLint("BuildNotImplemented")
    private fun showDatePicker() {
        SimpleDateDialog.build()
            .date(calendar.time)
            .neg(R.string.cancel)
            .pos(R.string.ok)
            .show(this, DATE_PICKER_TAG)
    }

    @SuppressLint("BuildNotImplemented")
    private fun showTimePicker() {
        SimpleTimeDialog.build()
            .hour(calendar.get(Calendar.HOUR_OF_DAY))
            .minute(calendar.get(Calendar.MINUTE))
            .set24HourView(DateFormat.is24HourFormat(context))
            .neg(R.string.cancel)
            .pos(R.string.ok)
            .show(this, TIME_PICKER_TAG)
    }

    private fun setDateField(calendar: Calendar) {
        date.setText(DateFormat.getDateFormat(context).format(calendar.time))
    }

    private fun setTimeField(calendar: Calendar) {
        time.setText(DateFormat.getTimeFormat(context).format(calendar.time))
    }

    companion object {
        private const val TIME_PICKER_TAG = "TIME_PICKER"
        private const val DATE_PICKER_TAG = "DATE_PICKER"
    }
}
