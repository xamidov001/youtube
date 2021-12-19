package uz.pdp.youtube.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.pdp.youtube.fragments.*

class ViewAdapter(fragmentActivity: FragmentActivity,var listener : OpenHelper): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                HomeFragment(listener)
            }
            2 -> {
                AddFragment()
            }
            3 -> {
                SubscriptionFragment()
            }
            4 -> {
                LibraryFragment()
            }
            else -> {
                HomeFragment(listener)
            }
        }
    }
}