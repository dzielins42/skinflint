package pl.dzielins42.skinflint.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import pl.dzielins42.skinflint.data.entity.Transaction

interface TransactionRepository {
    fun getAll(): Flowable<List<Transaction>>

    fun insert(transaction: Transaction): Single<Long>

    fun insert(vararg transactions: Transaction): Single<List<Long>>

    fun update(transaction: Transaction): Completable

    fun update(vararg transactions: Transaction): Completable

    fun delete(transaction: Transaction): Completable

    fun delete(vararg transactions: Transaction): Completable

    fun delete(id: Long): Completable
}