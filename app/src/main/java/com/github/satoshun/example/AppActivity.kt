package com.github.satoshun.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.satoshun.example.databinding.AppActBinding
import com.github.satoshun.example.databinding.AppFragBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppActivity : AppCompatActivity() {
  private lateinit var binding: AppActBinding

  @Inject lateinit var gitHubApi: GitHubApi

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.app_act)

    DaggerAppComponent.factory().create(application).inject(this)

    lifecycleScope.launch {
      runCatching { gitHubApi.test() }
        .onFailure { println(it) }
      println("test")
    }

//    gitHubApi.test2()
//      .enqueue(object : retrofit2.Callback<Unit> {
//        override fun onFailure(call: Call<Unit>, t: Throwable) {
//          println(t)
//        }
//
//        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//          println(call)
//        }
//      })
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
