package williamlopes.project.rtcontrol.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase
import williamlopes.project.rtcontrol.model.User


class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()
    private var usuario: User? = null
    private var autenticacao: FirebaseAuth? = null
    //private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        autenticacao = ConfiguracaoFirebase.firebaseAutenticacao
        autenticacao?.signOut()

        setupOButtonClickListener()
        observe()
        switchListener()
    }

    private fun switchListener() =
        switch_access.setOnCheckedChangeListener { buttonView, isChecked ->
            tv_opening.text = getString(R.string.welcome_sigin)
            if (isChecked) {
                tv_opening.text = getString(R.string.welcome_signup)
            }
        }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = autenticacao?.currentUser
        //verifyUser(currentUser)
    }

    private fun setupOButtonClickListener() {
        button_access.setOnClickListener {
            val email = edit_cadastro_email.text.toString()
            val password = edit_cadastro_senha.text.toString()
            //loginViewModel.signUp(email, password)
        }
    }


    private fun signUp(email:String, password:String) {
        autenticacao?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = autenticacao?.currentUser
                    //verifyUser(user)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //verifyUser(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        autenticacao?.signInWithEmailAndPassword(
            email, password
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@LoginActivity, "Sucesso ao logar!", Toast.LENGTH_SHORT
                ).show()
                //openMainScreen()
            } else {
                Toast.makeText(
                    this@LoginActivity, "Erro ao logar: ${task.exception}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /*private fun verifyUser(actualUser: FirebaseUser?) {
        val actualUser = autenticacao?.currentUser
        if (actualUser != null) {
            openMainScreen()
        }
    }*/

    //private fun openMainScreen() = startActivity(Intent(applicationContext, HomeActivity::class.java))

    private fun observe(){

        /*loginViewModel.isUpdating.observe(this) {

        }*/

        loginViewModel.isUserCreated.observe(this) {isCreated ->
            if (isCreated){
                Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
            }

        }
    }


   /*private fun subscribe() {
        mainViewModel?.user?.let { bind(it, ::getUser) }
    }

    private fun getUser(user: User) {
        usuario = intent?.extras?.get("user") as User?
        usuario = user
    }*/

}
