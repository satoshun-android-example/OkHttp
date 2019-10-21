package com.github.satoshun.example.basiccache

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.example.databinding.BasicCacheFragBinding
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET

class BasicCacheFragment : Fragment() {
  private lateinit var binding: BasicCacheFragBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = BasicCacheFragBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    run {
      val okhttp = OkHttpClient.Builder()
        .build()

      val retrofit: Api = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okhttp)
        .build()
        .create()

      binding.noCache.setOnClickListener {
        lifecycleScope.launch {
          val r = retrofit.get()
          Log.d("priorResponse", r.raw().priorResponse.toString())
          Log.d("cacheResponse", r.raw().cacheResponse.toString())
          Log.d("networkResponse", r.raw().networkResponse.toString())
        }
      }
    }

    run {
      val okhttp = OkHttpClient.Builder()
        .addNetworkInterceptor(object : Interceptor {
          override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            return chain.proceed(
              request
                .newBuilder()
                .build()
            )
          }
        })
        .cache(Cache(requireActivity().cacheDir, (10 * 1024 * 1024).toLong()))
        .build()

      val retrofit: Api = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okhttp)
        .build()
        .create()

      binding.cache.setOnClickListener {
        lifecycleScope.launch {
          val r = retrofit.get()
          Log.d("priorResponse", r.raw().priorResponse.toString())
          Log.d("cacheResponse", r.raw().cacheResponse.toString())
          Log.d("networkResponse", r.raw().networkResponse.toString())
        }
      }
    }
  }
}

interface Api {
  @GET("users/satoshun")
  suspend fun get(): Response<Unit>
}
