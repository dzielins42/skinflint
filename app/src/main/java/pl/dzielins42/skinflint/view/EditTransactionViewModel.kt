package pl.dzielins42.skinflint.view

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pl.dzielins42.skinflint.data.entity.Transaction
import pl.dzielins42.skinflint.data.repository.TransactionRepository

class EditTransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
    }

    fun saveTransaction(transaction: Transaction) {
        // TODO this should be moved to interactor
        val operation = if (transaction.id == 0L) {
            transactionRepository.insert(transaction).ignoreElement()
        } else transactionRepository.update(
            transaction
        )
        compositeDisposable.add(
            operation
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