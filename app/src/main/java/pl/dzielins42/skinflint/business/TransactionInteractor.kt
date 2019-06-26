package pl.dzielins42.skinflint.business

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.dzielins42.skinflint.data.entity.Transaction
import pl.dzielins42.skinflint.data.repository.TransactionRepository
import java.util.*

class TransactionInteractor(
    private val transactionRepository: TransactionRepository
) {

    fun getForEdit(id: Long): Single<TransactionInputForm> {
        // TODO repoisitory to get specified id
        return getAll().firstOrError().map { list ->
            val transaction = list.firstOrNull { it.id == id }
            if (transaction == null) {
                prepareEmptyTransactionInputForm()
            } else {
                convertTransactionToInputForm(transaction)
            }
        }.subscribeOn(Schedulers.io())
    }

    fun delete(id:Long):Completable{
        return transactionRepository.delete(id)
            .subscribeOn(Schedulers.io())
    }

    fun save(input: TransactionInputForm): Single<Result<TransactionInputForm>> {
        return Single.just(input)
            .map { validateInput(it) }
            .flatMap {
                if (it.isValid()) {
                    saveInternal(input)
                        .andThen(Single.just(Result.done(it)))
                } else {
                    Single.just(Result.error(it))
                }
            }.subscribeOn(Schedulers.io())
    }

    fun getAll(): Flowable<List<Transaction>> {
        return transactionRepository.getAll().subscribeOn(Schedulers.io())
    }

    private fun saveInternal(input: TransactionInputForm): Completable {
        return Single.just(input).map {
            Transaction(
                it.id,
                it.name.value,
                it.currency.value,
                it.description.value,
                it.date.value.time,
                // TODO multiplier may depend on currency
                (it.value.value * 100).toLong()
            )
        }.flatMapCompletable {
            if (it.id == 0L) {
                transactionRepository.insert(it).ignoreElement()
            } else {
                transactionRepository.update(it)
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun validateInput(input: TransactionInputForm): TransactionInputForm {
        return TransactionInputForm(
            input.id,
            input.name.validate {
                when {
                    it.isBlank() -> InputFormField.ERROR_EMPTY
                    else -> InputFormField.ERROR_NONE
                }
            },
            input.currency.validate {
                InputFormField.ERROR_NONE
            },
            input.description.validate {
                InputFormField.ERROR_NONE
            },
            input.date.validate {
                InputFormField.ERROR_NONE
            },
            input.value.validate {
                if (it <= 0 || it.isNaN()) TransactionInputForm.ERROR_INVALID_VALUE
                else InputFormField.ERROR_NONE
            }
        )
    }

    private fun convertTransactionToInputForm(
        transaction: Transaction
    ): TransactionInputForm {
        return TransactionInputForm(
            transaction.id,
            InputFormField(transaction.name),
            InputFormField(transaction.currency),
            InputFormField(transaction.description ?: ""),
            InputFormField(Calendar.getInstance().apply { time = transaction.date }),
            // TODO multiplier may depend on currency
            InputFormField(transaction.value / 100.0)
        )
    }

    private fun prepareEmptyTransactionInputForm(): TransactionInputForm {
        return TransactionInputForm(
            0L,
            InputFormField(""),
            // TODO default currency
            InputFormField("PLN"),
            InputFormField(""),
            InputFormField(Calendar.getInstance()),
            InputFormField(0.0)
        )
    }
}