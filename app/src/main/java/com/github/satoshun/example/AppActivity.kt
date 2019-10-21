package com.github.satoshun.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.satoshun.example.databinding.AppActBinding
import com.github.satoshun.example.databinding.AppFragBinding

class AppActivity : AppCompatActivity() {
  private lateinit var binding: AppActBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.app_act)
  }
}

class AppFragment : Fragment() {
  private lateinit var binding: AppFragBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = AppFragBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.basicCache.setOnClickListener {
      findNavController().navigate(
        AppFragmentDirections.navAppToBasicCache()
      )
    }

    binding.picassoCache.setOnClickListener {
      findNavController().navigate(
        AppFragmentDirections.navAppToPicassoCache()
      )
    }
  }
}
