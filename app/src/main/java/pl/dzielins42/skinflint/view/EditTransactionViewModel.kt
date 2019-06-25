package pl.dzielins42.skinflint.view

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import pl.dzielins42.skinflint.business.TransactionInteractor
import pl.dzielins42.skinflint.data.entity.Transaction

class EditTransactionViewModel(
    private val transactionInteractor: TransactionInteractor
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
    }

    fun saveTransaction(transaction: Transaction) {
        compositeDisposable.add(
            transactionInteractor.save(transaction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}