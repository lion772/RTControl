package williamlopes.project.rtcontrol.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import williamlopes.project.rtcontrol.repository.UserRepository
import williamlopes.project.rtcontrol.viewmodel.LoginViewModel
import williamlopes.project.rtcontrol.usecase.UserUseCase
import williamlopes.project.rtcontrol.view.SignInActivity
import williamlopes.project.rtcontrol.view.SignUpActivity

object Module {
    val moduleDI = module {
        single { UserRepository(
            signupActivity = get(),
            signinActivity = get()
        )}
        single { UserUseCase(userRepository = get()) }
        single { SignUpActivity() }
        single { SignInActivity() }

        viewModel { LoginViewModel(userUseCase = get()) }
    }


}