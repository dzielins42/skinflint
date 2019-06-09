package pl.dzielins42.skinflint.data.source

import androidx.room.*
import androidx.room.RoomDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import pl.dzielins42.skinflint.data.entity.Transaction
import pl.dzielins42.skinflint.data.repository.TransactionRepository
import java.util.*

@Database(entities = [Transaction::class], version = 1)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}

@Dao
interface TransactionDao : TransactionRepository {
    @Query("SELECT * FROM transactions")
    override fun getAll(): Flowable<List<Transaction>>

    @Insert
    override fun insert(transaction: Transaction): Single<Long>

    @Insert
    override fun insert(vararg transactions: Transaction): Single<List<Long>>

    @Update
    override fun update(transaction: Transaction): Completable

    @Update
    override fun update(vararg transactions: Transaction): Completable

    @Delete
    override fun delete(transaction: Transaction): Completable

    @Delete
    override fun delete(vararg transactions: Transaction): Completable
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}


