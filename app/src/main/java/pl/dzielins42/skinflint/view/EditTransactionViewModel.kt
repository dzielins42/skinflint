package pl.dzielins42.skinflint.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pl.dzielins42.skinflint.data.entity.Transaction
import pl.dzielins42.skinflint.data.repository.TransactionRepository

class EditTransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val viewState: LiveData<TransactionsListViewState>
        get() = mutableViewState

    private val mutableViewState: MutableLiveData<TransactionsListViewState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
    }

    fun saveTransaction(transaction: Transaction) {
        compositeDisposable.add(
            transactionRepository.insert(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}