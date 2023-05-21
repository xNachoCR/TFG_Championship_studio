package com.example.tfg_championship_studio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tfg_championship_studio.databinding.ActivityMainBnvBinding


class BottomMainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBnvBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBnvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Equipos())

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_teams ->{
                    replaceFragment(Equipos())
                    true
                }
                R.id.menu_bracket ->{
                    replaceFragment(Bracket())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
    }

}

