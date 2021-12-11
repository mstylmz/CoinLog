package com.yilmaz.coinlog.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.config.Config
import com.yilmaz.coinlog.databinding.FragmentDashboardBinding
import com.yilmaz.coinlog.model.models.info.CoinInfo
import com.yilmaz.coinlog.model.models.listing.Coin

class DashboardFragment : Fragment(), CoinListAdapter.ClickListener {
    private val TAG = DashboardFragment::class.java.name

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var dashboardViewModel = DashboardViewModel()

    private var config = Config()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = CoinListAdapter(arrayListOf())
        val recyclerView = binding.coinList
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,
            false)

        val refreshView: ProgressBar = binding.progressBarRefresh

        dashboardViewModel.init()
        dashboardViewModel.getCoinLatest()?.observe(viewLifecycleOwner){
            it.coin.forEach(System.out::println)
            adapter.updateCoinList(it.coin as ArrayList<Coin>)
        }

        dashboardViewModel.getCoinsMetaData()?.observe(viewLifecycleOwner){
            adapter.setInfos(it)
        }

        dashboardViewModel.getRefreshStatus()?.observe(viewLifecycleOwner){
            Log.d(TAG,"refresh $it")
            if(!it)
                refreshView.visibility = View.VISIBLE
            else
                refreshView.visibility = View.GONE
        }
        dashboardViewModel.getRefreshError()?.observe(viewLifecycleOwner){
            if(!it.equals("NO_ERROR")){
                refreshView.visibility = View.GONE
                val toast = Toast.makeText(context, "$it ERROR", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.refreshCoinLatest(config.get_coin_limit())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun itemClickListener(coin: Coin, info: CoinInfo) {
        Log.d(TAG, "onButtonClick $coin")
        Log.d(TAG, "onButtonClick $info")
        val bottomSheetDialog:BottomSheetDialog
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)

    }

    override fun itemFavoriteClickListener(coin: Coin) {
        Log.d(TAG, "favoriteClick $coin")
    }

}