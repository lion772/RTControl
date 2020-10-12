package williamlopes.project.rtcontrol.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase
import williamlopes.project.rtcontrol.ui.viewmodel.LoginViewModel

class SignUpActivity : BaseActivity() {
    private val viewModel: LoginViewModel by viewModel()
    private var autenticacao: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        autenticacao = ConfiguracaoFirebase.firebaseAutenticacao
        autenticacao?.signOut()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        observe()
        btnSetClickListener()
    }

    override fun onStart() {
        super.onStart()
        verifyUser()
    }

    private fun verifyUser() {
        val actualUser = autenticacao?.currentUser
        actualUser?.let { openMainScreen() }
    }

    private fun observe(){

        viewModel.isUserCreated.observe(this) { isCreated ->
            if (isCreated){
                userRegisteredSuccess()
            } else {
                Toast.makeText(this,  getString(R.string.signup_fail), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading->
            isLoading?.let {
                if (it){
                    showProgressDialog(resources.getString(R.string.please_wait))
                } else{
                    hideProgressDialog()
                }
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_sign_up_activity.setNavigationOnClickListener{ onBackPressed()}
    }

    private fun btnSetClickListener(){
        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }

    private fun userRegisteredSuccess(){
        Toast.makeText(
            this@SignUpActivity,
            getString(R.string.signed_up_with_success),
            Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    @ExperimentalCoroutinesApi
    fun registerUser(){
        val name = et_name.text.toString().trim{ it <= ' '}
        val email = et_email.text.toString().trim{ it <= ' '}
        val password = et_password.text.toString().trim{ it <= ' '}

        if(validateForm(name, email, password)){
            viewModel.signUp(name, email, password)
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Por favor, digite um nome.")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Por favor, digite um email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Por favor, digite uma senha.")
                false

            } else -> true
        }
    }

    private fun openMainScreen() = startActivity(Intent(applicationContext, HomeActivity::class.java))

}