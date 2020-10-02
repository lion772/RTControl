package williamlopes.project.rtcontrol.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.ui.home.IntroActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorSplash)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.my_fragment).navigateUp()

    override fun onPause() {
        super.onPause()
        finish()
    }

}