package hr.algebra.ivanabilic.nba

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import hr.algebra.ivanabilic.nba.databinding.FragmentConfigureBinding
import hr.algebra.ivanabilic.nba.databinding.FragmentPlayersBinding
import hr.algebra.ivanabilic.nba.framework.fetchPlayers
import hr.algebra.ivanabilic.nba.model.Player

class PlayersFragment : Fragment() {

    private lateinit var binding: FragmentPlayersBinding
    private lateinit var players: MutableList<Player>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentPlayersBinding.inflate(inflater,container,false)
        players=requireContext().fetchPlayers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPlayers.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=PlayersAdapter(context,players)
        }
    }


}