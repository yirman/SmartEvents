package com.german.events.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.german.events.ui.fragment.MyEventsFragment
import com.german.events.ui.fragment.PublicEventsFragment

class PagerAdapter(fm:FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PublicEventsFragment.newInstance()
            2 -> MyEventsFragment.newInstance()
            else -> Fragment()
        }
    }
}