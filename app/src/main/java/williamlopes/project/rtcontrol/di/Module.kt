package williamlopes.project.rtcontrol.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import williamlopes.project.rtcontrol.base.BaseViewModel
import williamlopes.project.rtcontrol.repository.UserRepository
import williamlopes.project.rtcontrol.ui.home.HomeActivity
import williamlopes.project.rtcontrol.ui.home.LoginViewModel
import williamlopes.project.rtcontrol.usecase.UserUseCase
import williamlopes.project.rtcontrol.ui.home.SignInActivity
import williamlopes.project.rtcontrol.ui.home.SignUpActivity

object Module {
    val moduleDI = module {
        single { UserUseCase(userRepository = get()) }
        single { SignUpActivity() }
        single { SignInActivity() }
        single { HomeActivity() }
        single { UserRepository(
            signupActivity = get(),
            signinActivity = get(),
            homeActivity = get()
        )}

        viewModel { LoginViewModel(userUseCase = get()) }
        viewModel { BaseViewModel() }
    }


}