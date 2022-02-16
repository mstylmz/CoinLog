package com.yilmaz.coinlog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yilmaz.coinlog.databinding.ActivityMainBinding
import com.yilmaz.coinlog.ui.SectionsPagerAdapter
import com.yilmaz.coinlog.ui.coinlist.CoinListFragment
import com.yilmaz.coinlog.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

        val homeFragment = HomeFragment()
        val dashboardFragment = CoinListFragment()

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager

        sectionsPagerAdapter.addFragment(homeFragment, resources.getString(R.string.title_home))
        sectionsPagerAdapter.addFragment(dashboardFragment, resources.getString(R.string.title_dashboard))

        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 3
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }
}