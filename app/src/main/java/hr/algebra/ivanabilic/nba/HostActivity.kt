package hr.algebra.ivanabilic.nba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import hr.algebra.ivanabilic.nba.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        binding= ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initHamburgerMenu()
        initNavigation()
    }



    private fun initHamburgerMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

    }


    companion object{
        lateinit var menu: Menu
    }


    private fun initNavigation() {
        menu=binding.navigationView.menu
        var navController= Navigation.findNavController(this,R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.navigationView,navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.host_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuExit->{
                exitApp()
                return true
            }
            android.R.id.home->{
                toggleDrawer()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitApp() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.exit)
            setMessage(getString(R.string.really))
            setIcon(R.drawable.power)
            setCancelable(true)
            setNegativeButton(getString(R.string.cancel),null)
            setPositiveButton(getString(R.string.ok)){ _, _->finish()}
            show()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawers()
        }
        else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}