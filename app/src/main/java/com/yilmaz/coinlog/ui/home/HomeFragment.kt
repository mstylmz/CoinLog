package com.yilmaz.coinlog.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yilmaz.coinlog.config.Config
import com.yilmaz.coinlog.databinding.FragmentHomeBinding
import com.yilmaz.coinlog.model.models.listing.Coin

class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.java.name

    private var _binding: FragmentHomeBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var coinList = ArrayList<Coin>()
    private var homeViewModel = HomeViewModel()
    private var config = Config()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = TopCoinListAdapter(arrayListOf())
        val recyclerView = binding.listTopCoin
        val refreshView: ProgressBar = binding.progressBarTopCoin

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,
            false)

        homeViewModel.init()
        homeViewModel.getRefreshStatus()?.observe(viewLifecycleOwner){
            Log.d(TAG,"refresh $it")
            if(!it)
                refreshView.visibility = View.VISIBLE
            else
                refreshView.visibility = View.GONE
        }

        homeViewModel.getTopCoinList()?.observe(viewLifecycleOwner){
            adapter.updateCoinList(it)
        }

        homeViewModel.getCoinsMetaData()?.observe(viewLifecycleOwner){
            adapter.setInfos(it)
            it.metaData.forEach{
                Log.d(TAG, it.toString())
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.refreshCoinLatest(config.get_top_coin_limit())
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
    }
}