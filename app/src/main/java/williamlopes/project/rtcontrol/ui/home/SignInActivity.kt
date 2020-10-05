package williamlopes.project.rtcontrol.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase
import williamlopes.project.rtcontrol.model.User

class SignInActivity : BaseActivity() {
    private val viewModel: LoginViewModel by viewModel()
    private var autenticacao: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        autenticacao = ConfiguracaoFirebase.firebaseAutenticacao
        //autenticacao?.signOut()

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
        //verifyUser(currentUser)
    }

    private fun verifyUser(actualUser: FirebaseUser?) {
        val actualUser = autenticacao?.currentUser
        actualUser?.let { openMainScreen() }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_sign_in_activity)
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbar_sign_in_activity.setNavigationOnClickListener{ onBackPressed()}
    }

    fun signInSuccess(loggedInUser: User){
        openMainScreen()
        finish()
    }


    private fun observe(){

        viewModel.isUserCreated.observe(this) {user ->
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            signInSuccess(user)
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


    @ExperimentalCoroutinesApi
    private fun btnSetClickListener(){
        btn_sign_in.setOnClickListener {
            registerUser()
        }
    }

    @ExperimentalCoroutinesApi
    private fun registerUser(){
        val email = et_email_login.text.toString().trim{ it <= ' '}
        val password = et_password_login.text.toString().trim{ it <= ' '}

        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            viewModel.signIn(email, password)
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Por favor, digite um email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Por favor, digite uma senha.")
                false

            }
            else -> true
        }
    }


    private fun openMainScreen() = startActivity(Intent(applicationContext, HomeActivity::class.java))

}