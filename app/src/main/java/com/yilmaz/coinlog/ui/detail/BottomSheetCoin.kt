package com.yilmaz.coinlog.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yilmaz.coinlog.common.Common
import com.yilmaz.coinlog.databinding.BottomSheetCoinDialogBinding
import com.yilmaz.coinlog.model.models.info.CoinInfo
import com.yilmaz.coinlog.model.models.listing.Coin


class BottomSheetCoin(val mCoin: Coin, val mInfo: CoinInfo): BottomSheetDialogFragment() {

    private val TAG = BottomSheetCoin::class.java.name

    private var _binding: BottomSheetCoinDialogBinding? = null
    private val binding get() = _binding!!
    var root: View? = null
    var common: Common? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetCoinDialogBinding.inflate(layoutInflater)
        root = binding.root
        return root as View
    }

    companion object{
        fun newInstance(coin: Coin, info: CoinInfo): BottomSheetCoin
        {
            return BottomSheetCoin(coin, info)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coin = mCoin
        binding.info = mInfo

        common = Common()

        binding.constraintLayoutTwitter.setOnClickListener{
            if(mInfo.urls.twitter.isNotEmpty()){
                Log.d(TAG, "onViewCreated: " + mInfo.urls.twitter[0])
                openWebSite(mInfo.urls.twitter[0])
            }else{
                common!!.showToast(root!!.context, "Link not found!", Toast.LENGTH_LONG)
            }
        }

        binding.constraintLayoutReddit.setOnClickListener{
            if(mInfo.urls.reddit.isNotEmpty()){
                Log.d(TAG, "onViewCreated: " + mInfo.urls.reddit[0])
                openWebSite(mInfo.urls.reddit[0])
            }else{
                common!!.showToast(root!!.context, "Link not found!", Toast.LENGTH_LONG)
            }
        }
        binding.constraintLayoutWebsite.setOnClickListener{
            if(mInfo.urls.website.isNotEmpty()){
                Log.d(TAG, "onViewCreated: " + mInfo.urls.website[0])
                openWebSite(mInfo.urls.website[0])
            }else{
                common!!.showToast(root!!.context, "Link not found!", Toast.LENGTH_LONG)
            }
        }

        binding.constraintLayoutTechDocumentation.setOnClickListener{
            if(mInfo.urls.technical_doc.isNotEmpty()){
                Log.d(TAG, "onViewCreated: " + mInfo.urls.technical_doc[0])
                openWebSite(mInfo.urls.technical_doc[0])
            }else{
                common!!.showToast(root!!.context, "Link not found!", Toast.LENGTH_LONG)
            }
        }

        binding.constraintLayoutSourceCode.setOnClickListener{
            if(mInfo.urls.source_code.isNotEmpty()){
                Log.d(TAG, "onViewCreated: " + mInfo.urls.source_code[0])
                openWebSite(mInfo.urls.source_code[0])
            }else{
                common!!.showToast(root!!.context, "Link not found!", Toast.LENGTH_LONG)
            }
        }
        
        binding.imageViewSetAlarm.setOnClickListener{
            Log.d(TAG, "onViewCreated: imageViewSetAlarm click")
        }
        
        binding.imageViewBottomSheetFavorite.setOnClickListener{
            Log.d(TAG, "onViewCreated: imageViewBottomSheetFavorite clcik")
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding == null
    }

    fun openWebSite(url: String?) {
        if(url != null && url != ""){
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(url)
            startActivity(openURL)
        }else{
            Toast.makeText(context, "Link not found", Toast.LENGTH_LONG).show()
        }
    }
}