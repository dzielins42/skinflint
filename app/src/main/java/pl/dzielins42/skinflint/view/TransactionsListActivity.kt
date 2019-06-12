package pl.dzielins42.skinflint.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.dzielins42.skinflint.R

import kotlinx.android.synthetic.main.activity_transactions_list.*
import org.apache.commons.lang3.RandomStringUtils
import pl.dzielins42.skinflint.data.entity.Transaction
import java.util.*

class TransactionsListActivity : AppCompatActivity() {

    private val adapter = TransactionsAdapter()

    private var list = emptyList<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val newList = list.toMutableList()
            newList.add(
                Transaction(
                    list.size.toLong(),
                    RandomStringUtils.randomAlphabetic(8),
                    "$",
                    null,
                    Date(),
                    4200
                )
            )
            adapter.submitList(newList)
            list = newList
        }

        recyclerView.adapter = adapter
    }
}