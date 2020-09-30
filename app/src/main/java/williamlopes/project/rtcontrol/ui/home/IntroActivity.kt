package williamlopes.project.rtcontrol.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_intro.*
import williamlopes.project.rtcontrol.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btnSetup()
    }

    private fun btnSetup() {
        btn_sign_up_intro.setOnClickListener{
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }
        btn_sign_in_intro.setOnClickListener{
            startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
        }
    }
}