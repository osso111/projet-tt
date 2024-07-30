package com.example.elapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class OffersFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout2: TabLayout
    private val offers = listOf(
        Offer("https://www.tunisietelecom.tn/media/p2chjtee/1920x600-1.png", "https://www.tunisietelecom.tn/particulier/mobile/options-services/forfait3echra/"),
        Offer("https://www.tunisietelecom.tn/media/o42p1au2/1920x600.jpg", "https://www.tunisietelecom.tn/particulier/mobile/options-services/esim/"),
        Offer("https://www.tunisietelecom.tn/media/xzofkztr/bannière-centrale_1920x600-waffi.png", "https://www.tunisietelecom.tn/particulier/fixe-internet/promotions/waffirapido/")

    )

    private val offers2 = listOf(
        Offer("https://www.tunisietelecom.tn/media/dr1bdvai/banni%C3%A8re-centrale_1920x600.jpg", "https://www.tunisietelecom.tn/particulier/mobile/promotions/30giga/"),
        Offer("https://www.tunisietelecom.tn/media/lfzpes3y/roaming-pass-partout-1920x600-1.jpg", "https://www.tunisietelecom.tn/particulier/mobile/international-roaming/passpartout/"),
        Offer("https://www.tunisietelecom.tn/media/w5bhswzw/bannie-re-centrale-ipv6_1920x600-13.png", "https://myspace.tunisietelecom.tn/Pages/configipv6.aspx")
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager2 = view.findViewById(R.id.viewPager2)
        tabLayout2 = view.findViewById(R.id.tabLayout2)

        val adapter = ImageSliderAdapter(offers, requireContext())
        viewPager.adapter = adapter

        val adapter2 = ImageSliderAdapter(offers2, requireContext())
        viewPager2.adapter = adapter2

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomView(createTabView(position == 0))
        }.attach()

        TabLayoutMediator(tabLayout2, viewPager2) { tab, position ->
            tab.setCustomView(createTabView(position == 0))
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until tabLayout.tabCount) {
                    val tab = tabLayout.getTabAt(i)
                    if (tab != null && tab.customView != null) {
                        val imageView = tab.customView as ImageView
                        imageView.setImageResource(if (i == position) R.drawable.tab_indicator_selected else R.drawable.tab_indicator_unselected)
                    }
                }
            }
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until tabLayout2.tabCount) {
                    val tab = tabLayout2.getTabAt(i)
                    if (tab != null && tab.customView != null) {
                        val imageView = tab.customView as ImageView
                        imageView.setImageResource(if (i == position) R.drawable.tab_indicator_selected else R.drawable.tab_indicator_unselected)
                    }
                }
            }
        })
        // Auto-scroll handler
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (viewPager.currentItem == offers.size - 1) {
                viewPager.currentItem = 0
            } else {
                viewPager.currentItem += 1
            }
        }
        val handler2 = Handler(Looper.getMainLooper())
        val update2 = Runnable {
            if (viewPager2.currentItem == offers2.size - 1) {
                viewPager2.currentItem = 0
            } else {
                viewPager2.currentItem += 1
            }
        }

        val timer2 = Timer()
        timer2.schedule(object : TimerTask() {
            override fun run() {
                handler2.post(update2)
            }
        }, 5000, 5000)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 5000, 5000)

        return view
    }

    private fun createTabView(isSelected: Boolean): ImageView {
        val imageView = ImageView(context)
        imageView.setImageResource(if (isSelected) R.drawable.tab_indicator_selected else R.drawable.tab_indicator_unselected)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imageView.layoutParams = layoutParams
        return imageView
    }
}
