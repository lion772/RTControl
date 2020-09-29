package williamlopes.project.rtcontrol.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*
import williamlopes.project.rtcontrol.R

class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar_activity)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        val drawerLayout : DrawerLayout? = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.list_fragment),
            drawerLayout)

        setupActionBar(navController, appBarConfiguration)
        setupNavigationMenu(navController)
    }


    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {
        nav_view?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.host_fragment).navigateUp(appBarConfiguration)

}