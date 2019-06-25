package pl.dzielins42.skinflint.dagger

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDexApplication
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.dzielins42.skinflint.business.BusinessModule
import pl.dzielins42.skinflint.data.DataModule
import pl.dzielins42.skinflint.view.ViewModule
import javax.inject.Inject
import javax.inject.Singleton

class DaggerApp : MultiDexApplication(), HasActivityInjector {

    private lateinit var component: ApplicationComponent
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
            .application(this)
            .context(this)
            .build()
        component.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        DataModule::class,
        BusinessModule::class,
        ViewModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: DaggerApp): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: DaggerApp)
}

@Module
class ApplicationModule