package pl.dzielins42.skinflint.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_transactions_list.*
import pl.dzielins42.skinflint.R
import javax.inject.Inject

class TransactionsListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: TransactionsListViewModel

    private val adapter = TransactionsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_transactions_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            startActivity(EditTransactionActivity.getIntent(this))
            /*viewModel.saveTransaction(
                Transaction(
                    0,
                    RandomStringUtils.randomAlphabetic(8),
                    "$",
                    null,
                    Date(),
                    4200
                )
            )*/
        }

        recyclerView.adapter = adapter

        viewModel.viewState.observe(
            this,
            Observer<TransactionsListViewState> { viewState ->
                adapter.submitList(viewState.transactions)
            }
        )
    }
}