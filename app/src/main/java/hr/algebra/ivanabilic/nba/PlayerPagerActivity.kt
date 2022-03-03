package hr.algebra.ivanabilic.nba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import hr.algebra.ivanabilic.nba.databinding.ActivityPlayerPagerBinding
import hr.algebra.ivanabilic.nba.framework.fetchPlayers
import hr.algebra.ivanabilic.nba.model.Player

const val ITEM_POSITION="hr.algebra.ivanabilic.nba.item_position"
class PlayerPagerActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPlayerPagerBinding

    private var itemPosition=0
    private lateinit var players: MutableList<Player>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlayerPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //back
    }

    private fun initPager() {
        players= this.fetchPlayers()
        itemPosition=intent.getIntExtra(ITEM_POSITION,0)
        with(binding.viewPager){
            adapter=PlayersPagerAdapter(this@PlayerPagerActivity,players)
            currentItem=itemPosition
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}