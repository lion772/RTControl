package williamlopes.project.rtcontrol.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import williamlopes.project.rtcontrol.base.BaseViewModel
import williamlopes.project.rtcontrol.repository.UserRepository
import williamlopes.project.rtcontrol.ui.home.*
import williamlopes.project.rtcontrol.usecase.UserUseCase

object Module {
    val moduleDI = module {
        single { UserUseCase(userRepository = get()) }
        single { UserRepository()}

        viewModel { LoginViewModel(userUseCase = get()) }
        viewModel { HomeViewModel(userUseCase = get()) }
    }


}