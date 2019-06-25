package pl.dzielins42.skinflint.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import pl.dzielins42.skinflint.business.TransactionInteractor

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { transactions ->
                    mutableViewState.postValue(TransactionsListViewState((transactions)))
                }
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}