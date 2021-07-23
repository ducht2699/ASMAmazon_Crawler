package com.vnsoft.exam.injection

import com.vnsoft.exam.ui.login.LoginViewModel
import com.vnsoft.exam.ui.main.MainViewModel
import dagger.Component

/**
 * Component providing inject() methods for presenters.
 */
@Component(modules = [(NetworkModule::class)], dependencies = [ApplicationComponent::class])
@ViewModelScope
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param postListViewModel PostListViewModel in which to inject the dependencies
     */
    fun inject(loginViewModel: LoginViewModel)
    fun inject(homeViewModel: MainViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
    }
}