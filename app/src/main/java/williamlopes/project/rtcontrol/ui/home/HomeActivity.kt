package williamlopes.project.rtcontrol.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.helper.ConfiguracaoFirebase.getDataFromFireStore
import williamlopes.project.rtcontrol.model.User

class HomeActivity: AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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


    /*suspend fun updateNavigationUserDetails(loggedInUser: User, activity: HomeActivity) {
        try {
            Glide.with(this).load(loggedInUser.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(nav_user_image)

            tv_username.text = loggedInUser.name
            //signInUserIntoFirebase(activity)
        } catch (e: Exception) {
            e.cause
        }
    }*/

    @ExperimentalCoroutinesApi
    suspend fun updateNavigationUserDetails(loggedInUser: User, activity: HomeActivity) {
        try {
            Glide.with(this)
                .load(loggedInUser.image)
                .centerCrop()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_user_place_holder)
                .into(nav_user_image)

            tv_username.text = loggedInUser.name
           getDataFromFireStore(this)
        } catch (e: Exception) {
            e.cause
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
                startActivity(intent).also { finish() }
                true
            }
        } ?: run { false }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.host_fragment).navigateUp(appBarConfiguration)

}