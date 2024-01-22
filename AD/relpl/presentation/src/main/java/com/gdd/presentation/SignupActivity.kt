package com.gdd.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.gdd.presentation.base.BaseActivity
import com.gdd.presentation.databinding.ActivityMainBinding
import com.gdd.presentation.databinding.ActivitySignupBinding
import com.gdd.presentation.signup.DepthPageTransformer
import com.gdd.presentation.signup.SignupViewModel
import com.gdd.presentation.signup.SignupViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(
    ActivitySignupBinding::inflate
) {
    private val viewModel: SignupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewPager()

        binding.btnBack.setOnClickListener {
            moveToPrevPage()
        }
    }

    private fun initViewPager(){
        val viewPagerAdapter = SignupViewPagerAdapter(this)
        binding.vpInput.apply {
            adapter = viewPagerAdapter
            setPageTransformer(DepthPageTransformer())
            binding.indicator.attachTo(this)
            isUserInputEnabled = false
        }
    }

    fun moveToNextPage(){
        when(binding.vpInput.currentItem){
            4 -> {
                Toast.makeText(this, "회원가입!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.vpInput.currentItem += 1
            }
        }
    }

    fun moveToPrevPage(){
        when(binding.vpInput.currentItem){
            0 -> {
                Toast.makeText(this, "첫 페이지 입니다", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.vpInput.currentItem -= 1
            }
        }
    }
}