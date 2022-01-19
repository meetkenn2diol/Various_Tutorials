package com.codelabs.state.viewpager2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.codelabs.state.R
import com.codelabs.state.advancedcoroutine.ui.MainPlantActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPager2Fragment : Fragment() {
    private var navController: NavController? = null

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
        }
    }

    //tabLayout used to give the viewpager a title tab
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_viewpager2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewPager2 = view.findViewById(R.id.viewpager2)
        viewPager2.adapter = ViewPager2Adapter(requireContext())
        tabLayout = view.findViewById(R.id.tab_layout)

        //TabLayoutMediator used to define how viewPager2 and tabLayout will work together
        TabLayoutMediator(tabLayout, viewPager2, true, true) { tab, position ->
            tab.text = "Page $position"
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.goto_viewpager2_demo).isVisible = false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.goto_firebaseui_demo -> {
                navController!!.navigate(R.id.action_viewPager2Fragment_to_mainFragment)
                true
            }
            R.id.goto_advanced_coroutine_tutorial->{
                val intent = Intent(requireActivity(), MainPlantActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onPause() {
        super.onPause()
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
    }
}