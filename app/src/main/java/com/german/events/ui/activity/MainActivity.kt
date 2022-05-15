package com.german.events.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.german.events.R
import com.german.events.databinding.ActivityMainBinding
import com.german.events.ui.adapter.PagerAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var firebaseAuth: FirebaseAuth? = null
        @Inject set

    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI(){

        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.menu.getItem(position).isChecked = true

            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_public_events -> {
                    binding.viewPager.currentItem = 0
                    return@setOnItemSelectedListener true
                }

                R.id.nav_subscriptions -> {
                    binding.viewPager.currentItem = 1
                    return@setOnItemSelectedListener true
                }

                R.id.nav_my_events -> {
                    binding.viewPager.currentItem = 2
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        binding.logout.setOnClickListener {
            firebaseAuth?.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}