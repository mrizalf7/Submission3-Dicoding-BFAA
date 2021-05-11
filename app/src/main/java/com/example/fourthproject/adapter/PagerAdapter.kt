package com.example.fourthproject.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fourthproject.fragment.FollowersFragment
import com.example.fourthproject.fragment.FollowingFragment

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username:String? =""
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
         if (position == 0) {
             fragment = FollowersFragment.newInstance(username)
         }
         else if (position == 1){
             fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }
}