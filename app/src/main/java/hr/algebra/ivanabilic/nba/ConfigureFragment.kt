package hr.algebra.ivanabilic.nba

import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.ivanabilic.nba.databinding.FragmentConfigureBinding

class ConfigureFragment : Fragment() {

    private lateinit var binding:FragmentConfigureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentConfigureBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.rvItems.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=ItemsAdapter(requireContext(),prepareList(HostActivity.menu))
        }
    }

    private fun prepareList(menu: Menu) :MutableList<MenuItem> {
        val mutableListOf = mutableListOf<MenuItem>()
        for (index in 0 until menu.size()){
            if(menu.getItem(index).itemId!=R.id.menuConfigure){
                mutableListOf.add(menu.getItem(index))
            }
        }
        return mutableListOf

    }
}