package williamlopes.project.rtcontrol.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    fun delayToNextScreen(block: () -> Unit) {
        viewModelScope.launch {
            delay(1500)
            block()
        }
    }
}