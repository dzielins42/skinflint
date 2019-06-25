package pl.dzielins42.skinflint.view.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import pl.dzielins42.skinflint.business.TransactionInteractor
import pl.dzielins42.skinflint.dagger.ViewModelKey
import pl.dzielins42.skinflint.view.transactions.details.EditTransactionActivity
import pl.dzielins42.skinflint.view.transactions.details.EditTransactionViewModel
import pl.dzielins42.skinflint.view.transactions.list.TransactionsListActivity
import pl.dzielins42.skinflint.view.transactions.list.TransactionsListViewModel

@Module(
    includes = [
        TransactionsModule.BindView::class,
        TransactionsModule.ProvideViewModel::class
    ]
)
abstract class TransactionsModule {

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
            transactionInteractor: TransactionInteractor
        ): ViewModel = TransactionsListViewModel(transactionInteractor)

        @Provides
        @IntoMap
        @ViewModelKey(EditTransactionViewModel::class)
        fun provideEditTransactionViewModel(
            transactionInteractor: TransactionInteractor
        ): ViewModel = EditTransactionViewModel(transactionInteractor)
    }
}