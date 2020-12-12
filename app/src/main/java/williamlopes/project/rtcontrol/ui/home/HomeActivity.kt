package williamlopes.project.rtcontrol.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.databinding.ActivityHomeBinding
import williamlopes.project.rtcontrol.databinding.FragmentMyProfileBinding
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.ui.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity(), MyProfileFragmentListener {
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var databinding: ActivityHomeBinding
    private lateinit var user: User

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setNavigationSettings()
        setupObservable()
        viewModel.getUser()
    }

    private fun setupObservable() {
        viewModel.user.observe(this, { user ->
            this.databinding.user = user
            updateNavigationUserDetails(Uri.parse(user.image))
        })
    }

    private fun setNavigationSettings() {
        setSupportActionBar(toolbar_activity)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.listFragment),
            drawerLayout
        )

        setupActionBar(navController, appBarConfiguration)
        setupNavigationMenu(navController)
    }

    private fun updateNavigationUserDetails(userImage: Uri) {
        try {
            Glide.with(this)
                .load(userImage)
                .centerCrop()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_user_place_holder)
                .into(iv_user_profile)

            tv_username.text = user.name

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {
        nav_view?.apply {
            setupWithNavController(navController)

            menu.findItem(R.id.sign_out).setOnMenuItemClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@HomeActivity, IntroActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent).also {
                    finish()
                }
                true  
            }
        } ?: run { false }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.host_fragment).navigateUp(appBarConfiguration)

    companion object {
        private const val MY_PROFILE_REQUEST_CODE = 11
        private const val SDK_VALUE = 23
        private const val REQUEST_CODE = 1

    }

    override fun listenerProfileImage(uri: Uri) {
        try {
            Glide.with(this)
                .load(uri)
                .centerCrop()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_user_place_holder)
                .into(iv_user_profile)

            tv_username.text = user.name

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}