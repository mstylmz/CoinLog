package com.yilmaz.coinlog.ui.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.config.Config
import com.yilmaz.coinlog.databinding.BottomSheetCoinDialogBinding
import com.yilmaz.coinlog.databinding.FragmentDashboardBinding
import com.yilmaz.coinlog.model.models.info.CoinInfo
import com.yilmaz.coinlog.model.models.listing.Coin


class DashboardFragment : Fragment(), CoinListAdapter.ClickListener {
    private val TAG = DashboardFragment::class.java.name

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var dashboardViewModel = DashboardViewModel()
    var root: View? = null
    private var config = Config()

    private lateinit var myClickHandlers: MyClickHandlers

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        root = binding.root
        //adapter
        val adapter = CoinListAdapter(arrayListOf())
        val recyclerView = binding.coinList
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
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

        return root as View
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

        /*
        bottomSheetDialog = BottomSheetDialog(root!!.context)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_coin_dialog, null)
        bottomSheetDialog!!.setContentView(view)
        bottomSheetDialog!!.show()*/
        val inflater = LayoutInflater.from(root!!.context)
        val binding = DataBindingUtil.inflate<BottomSheetCoinDialogBinding>(inflater,R.layout.bottom_sheet_coin_dialog,
            root as ViewGroup?, false)

        myClickHandlers = MyClickHandlers(binding.root.context)
        binding.coin  = coin
        binding.info = info
        binding.click = myClickHandlers

        if (!coin.max_supply.equals(0.0) && !coin.circulating_supply.equals(0.0)){
            val circulating_percent = (coin.circulating_supply/coin.max_supply) * 100
            binding.percentCirculatingSupply = String.format("%,.1f", circulating_percent) + "%"
        }else{
            binding.percentCirculatingSupply = "Unknown %"
        }

        val bottomSheetDialog = BottomSheetDialog(inflater.context)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }


    //click event for bottom sheet dialog
    class MyClickHandlers(context: Context) {
        private val TAG = MyClickHandlers::class.java.name
        var context: Context
        init {
            this.context = context
        }

        fun openWebSite(url: String?) {
            if(url != null && url != ""){
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(url)
                context.startActivity(openURL)
            }else{
                Toast.makeText(context, "Link not found", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun itemFavoriteClickListener(coin: Coin) {
        Log.d(TAG, "favoriteClick $coin")
    }
}