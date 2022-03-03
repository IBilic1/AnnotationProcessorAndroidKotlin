package hr.algebra.ivanabilic.nba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.algebra.ivanabilic.nba.databinding.ActivitySplashScreenBinding
import hr.algebra.ivanabilic.nba.framework.*
import hr.algebra.ivanabilic.nba.model.Player


private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.ivanabilic.nba.data_imported"

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
        redirect()

    }

    private fun redirect() {
        binding.ivSplash.startAnimation(R.anim.slide_down)
        binding.tvSplash.startAnimation(R.anim.bounce)
    }

    private fun setUpListeners() {

        if(getBooleanPreference(DATA_IMPORTED)){
            callDelayed(DELAY) {startActivity<HostActivity>()}
        }
        else{
            if (isOnline()){
                NbaService.enqueue(this)
            }
            else{
                binding.tvSplash.text=getString(R.string.no_internet)
                callDelayed(DELAY){finish()}
            }
        }

    }

}