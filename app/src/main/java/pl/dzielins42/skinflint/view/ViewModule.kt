package pl.dzielins42.skinflint.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import pl.dzielins42.skinflint.view.transactions.TransactionsModule
import javax.inject.Provider
import javax.inject.Singleton

@Module(
    includes = [
        TransactionsModule::class
    ]
)
class ViewModule {

    @Singleton
    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return requireNotNull(providers[modelClass] as Provider<T>).get() as T
            }
        }
    }
}