package com.github.satoshun.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.example.databinding.AppActBinding
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET

class AppActivity : AppCompatActivity() {
  private lateinit var binding: AppActBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.app_act)

    run {
      val okhttp = OkHttpClient.Builder()
        .addNetworkInterceptor(object : Interceptor {
          override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            return chain.proceed(
              request
                .newBuilder()
                .addHeader("Cache-Control", "max-stale=60")
                .build()
            )
          }
        })
        .build()

      val retrofit: Api = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okhttp)
        .build()
        .create()

      binding.maxStale.setOnClickListener {
        lifecycleScope.launch {
          val r = retrofit.get()
          Log.d("hoge", r.raw().toString())
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
        .cache(Cache(cacheDir, (10 * 1024 * 1024).toLong()))
        .build()

      val retrofit: Api = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okhttp)
        .build()
        .create()

      binding.cache.setOnClickListener {
        lifecycleScope.launch {
          val r = retrofit.get()
          Log.d("hoge", r.raw().toString())
        }
      }
    }
  }
}

interface Api {
  @GET("users/satoshun")
  suspend fun get(): Response<Unit>
}
