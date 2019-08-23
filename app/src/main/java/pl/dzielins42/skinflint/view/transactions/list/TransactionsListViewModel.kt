package pl.dzielins42.skinflint.view.transactions.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pl.dzielins42.skinflint.business.TransactionInteractor
import pl.dzielins42.skinflint.data.entity.Transaction

class TransactionsListViewModel(
    private val transactionInteractor: TransactionInteractor
) : ViewModel() {

    val viewState: LiveData<TransactionsListViewState>
        get() = mutableViewState

    private val mutableViewState: MutableLiveData<TransactionsListViewState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            transactionInteractor.getAll()
                // Convert from business model to view model
                .flatMapSingle { transactions ->
                    Flowable.fromIterable(transactions)
                        .map { transaction -> TransactionItem(transaction) }
                        .subscribeOn(Schedulers.computation())
                        .toList()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { transactions ->
                    mutableViewState.postValue(
                        TransactionsListViewState(
                            (transactions)
                        )
                    )
                }
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}