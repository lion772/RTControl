package williamlopes.project.rtcontrol.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.data.bind
import williamlopes.project.rtcontrol.databinding.ActivityMainBinding
import williamlopes.project.rtcontrol.service.model.User
import williamlopes.project.rtcontrol.view.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var usuario: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        subscribe()
    }

    private fun subscribe(){
        bind(mainViewModel.user, ::getUser)
    }
    private fun getUser(user: User){
        usuario = intent?.extras?.get("user") as User?
        usuario = user
    }
}
