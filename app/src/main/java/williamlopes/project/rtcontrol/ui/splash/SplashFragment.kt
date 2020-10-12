package williamlopes.project.rtcontrol.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.base.BaseFragment
import williamlopes.project.rtcontrol.ui.viewmodel.SplashViewModel

class SplashFragment : BaseFragment<SplashViewModel>(SplashViewModel::class) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.delayToNextScreen {
            context?.let {
                findNavController().navigate(R.id.next_action)
            }
        }
    }
}