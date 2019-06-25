package pl.dzielins42.skinflint.view.transactions.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_transactions_list.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.data.entity.Transaction
import pl.dzielins42.skinflint.view.transactions.details.EditTransactionActivity
import javax.inject.Inject

class TransactionsListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: TransactionsListViewModel

    private val adapter = TransactionsAdapter(object :
        ItemClickListener {
        override fun onItemClick(item: Transaction) {
            startEditTransactionActivity(item)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_transactions_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startEditTransactionActivity()
        }

        recyclerView.adapter = adapter

        viewModel.viewState.observe(
            this,
            Observer<TransactionsListViewState> { viewState ->
                adapter.submitList(viewState.transactions)
            }
        )
    }

    private fun startEditTransactionActivity(transaction: Transaction? = null) {
        startActivity(EditTransactionActivity.getIntent(this, transaction))
    }
}