package pl.dzielins42.skinflint.business

import dagger.Module
import dagger.Provides
import pl.dzielins42.skinflint.data.repository.TransactionRepository
import javax.inject.Singleton

@Module
class BusinessModule {

    @Singleton
    @Provides
    fun provideTransactioInteractor(
        transactionRepository: TransactionRepository
    ): TransactionInteractor {
        return TransactionInteractor(transactionRepository)
    }
}