package pl.dzielins42.skinflint.data.source

import org.junit.Test
import pl.dzielins42.skinflint.data.entity.Transaction
import java.util.*

class TransactionDaoTest : AbstractRoomDatabaseTest() {

    @Test
    fun getFromEmpty() {
        database.transactionDao().getAll()
            .test()
            .assertNoErrors()
            .assertNotComplete()
            .assertValueCount(1)
            .assertValue { value -> value.isEmpty() }
    }

    @Test
    fun insertAndGet() {
        val testSubscriber = database.transactionDao().getAll().test()

        database.transactionDao().insert(TRANSACTION).blockingGet()

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(2)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty() && value.size == 1 && value[0] == TRANSACTION
            }
    }

    @Test
    fun insertUpdateAndGet() {
        val testSubscriber = database.transactionDao().getAll().test()

        database.transactionDao().insert(TRANSACTION).blockingGet()
        database.transactionDao().update(TRANSACTION.copy(name = "changedName")).blockingAwait()

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(3)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty() && value.size == 1 && value[0] == TRANSACTION
            }
            .assertValueAt(2) { value ->
                value.isNotEmpty() && value.size == 1 && value[0].id == TRANSACTION.id && value[0].name == "changedName"
            }
    }

    @Test
    fun insertDeleteAndGet() {
        val testSubscriber = database.transactionDao().getAll().test()

        database.transactionDao().insert(TRANSACTION).blockingGet()
        database.transactionDao().delete(TRANSACTION).blockingAwait()

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(3)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty() && value.size == 1 && value[0] == TRANSACTION
            }
            .assertValueAt(2) { value ->
                value.isEmpty()
            }
    }

    @Test
    fun insertDeleteByIdAndGet() {
        val testSubscriber = database.transactionDao().getAll().test()

        database.transactionDao().insert(TRANSACTION).blockingGet()
        database.transactionDao().delete(TRANSACTION.id).blockingAwait()

        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertValueCount(3)
            .assertValueAt(0) { value ->
                value.isEmpty()
            }
            .assertValueAt(1) { value ->
                value.isNotEmpty() && value.size == 1 && value[0] == TRANSACTION
            }
            .assertValueAt(2) { value ->
                value.isEmpty()
            }
    }

    companion object {
        private val TRANSACTION = Transaction(
            123,
            "name",
            "$$$",
            "description",
            Date(),
            123
        )
    }
}