package pl.dzielins42.skinflint.view.transactions.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import pl.dzielins42.skinflint.business.TransactionInputForm
import pl.dzielins42.skinflint.business.TransactionInteractor

class EditTransactionViewModel(
    private val transactionInteractor: TransactionInteractor
) : ViewModel() {

    val viewState: LiveData<EditTransactionViewState>
        get() = mutableViewState

    private val mutableViewState: MutableLiveData<EditTransactionViewState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
    }

    fun deleteTransaction(id: Long) {
        compositeDisposable.add(
            transactionInteractor.delete(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    // TODO scan?
                    mutableViewState.postValue(
                        EditTransactionViewState(
                            mutableViewState.value!!.form, true
                        )
                    )
                }
        )
    }

    fun setEditedTransaction(id: Long) {
        compositeDisposable.add(
            transactionInteractor.getForEdit(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    mutableViewState.postValue(
                        EditTransactionViewState(
                            result, false
                        )
                    )
                }
        )
    }

    fun saveTransaction(input: TransactionInputForm) {
        compositeDisposable.add(
            transactionInteractor.save(input)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    mutableViewState.postValue(
                        EditTransactionViewState(
                            result.data!!, result.isDone()
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