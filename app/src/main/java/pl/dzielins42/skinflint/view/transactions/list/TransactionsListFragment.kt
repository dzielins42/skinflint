package pl.dzielins42.skinflint.view.transactions.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.android.support.AndroidSupportInjection
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_transactions_list.*
import pl.dzielins42.skinflint.R
import pl.dzielins42.skinflint.data.entity.Transaction
import javax.inject.Inject

class TransactionsListFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModel: TransactionsListViewModel

    private val adapter = FlexibleAdapter<IFlexible<*>>(emptyList(), this, true)

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transactions_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NavigationUI.setupWithNavController(toolbar, findNavController())

        fab.setOnClickListener {
            goToTransactionDetails()
        }

        recyclerView.adapter = adapter

        viewModel.viewState.observe(
            viewLifecycleOwner,
            Observer<TransactionsListViewState> { viewState ->
                adapter.updateDataSet(viewState.transactions)
            }
        )
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
    //endregion

    //region FlexibleAdapter.OnItemClickListener
    override fun onItemClick(view: View, position: Int): Boolean {
        when (val clickedItem = adapter.getItem(position)) {
            is TransactionItem -> goToTransactionDetails(clickedItem.transaction)
        }
        return false
    }
    //endregion

    private fun goToTransactionDetails(transaction: Transaction? = null) {
        val action = TransactionsListFragmentDirections.actionTransactionDetails(transaction?.id ?: 0L)
        findNavController().navigate(action)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TransactionsListFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}
