package pl.dzielins42.skinflint.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pl.dzielins42.skinflint.data.repository.TransactionRepository
import pl.dzielins42.skinflint.data.source.RoomDatabase
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(
        context: Context
    ): RoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RoomDatabase::class.java,
            "skinflint-db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTransactionRepository(
        roomDatabase: RoomDatabase
    ): TransactionRepository {
        return roomDatabase.transactionDao()
    }
}