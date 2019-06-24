package pl.dzielins42.skinflint.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import pl.dzielins42.skinflint.dagger.ViewModelKey
import pl.dzielins42.skinflint.data.repository.TransactionRepository

@Module(
    includes = [
        TransactionsListModule.BindView::class,
        TransactionsListModule.ProvideViewModel::class
    ]
)
abstract class TransactionsListModule {

    @Module(
        includes = [
            TransactionsListActivityBind::class,
            EditTransactionActivityBind::class
        ]
    )
    class BindView

    @Module
    abstract class TransactionsListActivityBind {

        @ContributesAndroidInjector(modules = [InjectViewModel::class])
        abstract fun bind(): TransactionsListActivity

        @Module
        class InjectViewModel {
            @Provides
            fun provideTransactionsListViewModel(
                factory: ViewModelProvider.Factory,
                target: TransactionsListActivity
            ) = ViewModelProviders.of(target, factory)
                .get(TransactionsListViewModel::class.java)
        }
    }

    @Module
    abstract class EditTransactionActivityBind {

        @ContributesAndroidInjector(modules = [InjectViewModel::class])
        abstract fun bind(): EditTransactionActivity

        @Module
        class InjectViewModel {
            @Provides
            fun provideEditTransactionViewModel(
                factory: ViewModelProvider.Factory,
                target: EditTransactionActivity
            ) = ViewModelProviders.of(target, factory)
                .get(EditTransactionViewModel::class.java)
        }
    }

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(TransactionsListViewModel::class)
        fun provideTransactionsListViewModel(
            transactionRepository: TransactionRepository
        ): ViewModel = TransactionsListViewModel(transactionRepository)

        @Provides
        @IntoMap
        @ViewModelKey(EditTransactionViewModel::class)
        fun provideEditTransactionViewModel(
            transactionRepository: TransactionRepository
        ): ViewModel = EditTransactionViewModel(transactionRepository)
    }
}