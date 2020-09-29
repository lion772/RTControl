package williamlopes.project.rtcontrol.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import williamlopes.project.rtcontrol.R

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        /*val typeFace: Typeface = Typeface.createFromAsset(assets,"carbon bl.ttf")
        tv_app_name.typeface = typeFace*/

        Handler().postDelayed({ abrirAutenticacao() }, 2000)
    }

    private fun abrirAutenticacao() {
        startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
        finish()
    }

}