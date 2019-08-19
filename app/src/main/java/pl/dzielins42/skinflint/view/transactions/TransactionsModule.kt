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
import pl.dzielins42.skinflint.view.transactions.details.TransactionDetailsViewModel
import pl.dzielins42.skinflint.view.transactions.details.TransactionDetailsFragment
import pl.dzielins42.skinflint.view.transactions.list.TransactionsListFragment
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
            TransactionsListFragmentBind::class,
            TransactionDetailsFragmentBind::class
        ]
    )
    class BindView

    @Module
    abstract class TransactionsListFragmentBind {

        @ContributesAndroidInjector(modules = [InjectViewModel::class])
        abstract fun bind(): TransactionsListFragment

        @Module
        class InjectViewModel {
            @Provides
            fun provideTransactionsListViewModel(
                factory: ViewModelProvider.Factory,
                target: TransactionsListFragment
            ) = ViewModelProviders.of(target, factory)
                .get(TransactionsListViewModel::class.java)
        }
    }

    @Module
    abstract class TransactionDetailsFragmentBind {

        @ContributesAndroidInjector(modules = [InjectViewModel::class])
        abstract fun bind(): TransactionDetailsFragment

        @Module
        class InjectViewModel {
            @Provides
            fun provideTransactionsListViewModel(
                factory: ViewModelProvider.Factory,
                target: TransactionDetailsFragment
            ) = ViewModelProviders.of(target, factory)
                .get(TransactionDetailsViewModel::class.java)
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
        @ViewModelKey(TransactionDetailsViewModel::class)
        fun provideEditTransactionViewModel(
            transactionInteractor: TransactionInteractor
        ): ViewModel = TransactionDetailsViewModel(transactionInteractor)
    }
}