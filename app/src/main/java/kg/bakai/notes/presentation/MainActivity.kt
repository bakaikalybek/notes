package kg.bakai.notes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kg.bakai.notes.R
import kg.bakai.notes.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.notesFragment -> {
                showBottomNav()
            }
            R.id.categoryFragment -> {
                showBottomNav()
            }
            else -> {
                hideBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        binding.bottomNavView.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavView.visibility = View.VISIBLE
    }
}