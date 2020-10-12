package williamlopes.project.rtcontrol.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import williamlopes.project.rtcontrol.repository.UserRepository
import williamlopes.project.rtcontrol.ui.viewmodel.HomeViewModel
import williamlopes.project.rtcontrol.ui.viewmodel.LoginViewModel
import williamlopes.project.rtcontrol.ui.viewmodel.MyProfileViewModel
import williamlopes.project.rtcontrol.usecase.UserUseCase

@ExperimentalCoroutinesApi
object Module {
    val moduleDI = module {
        single { UserUseCase(userRepository = get()) }
        single { UserRepository()}

        viewModel { LoginViewModel(userUseCase = get()) }
        viewModel { HomeViewModel(application = get(), userUseCase = get()) }
        viewModel { MyProfileViewModel(application = get(), userUseCase = get()) }
    }


}