package pl.dzielins42.skinflint.business

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import pl.dzielins42.skinflint.data.entity.Transaction
import pl.dzielins42.skinflint.data.repository.TransactionRepository

class TransactionInteractor(
    private val transactionRepository: TransactionRepository
) {

    fun save(transaction: Transaction): Completable {
        val operation = if (transaction.id == 0L) {
            transactionRepository.insert(transaction).ignoreElement()
        } else transactionRepository.update(
            transaction
        )

        return operation.subscribeOn(Schedulers.io())
    }

    fun getAll(): Flowable<List<Transaction>> {
        return transactionRepository.getAll().subscribeOn(Schedulers.io())
    }
}