package com.github.satoshun.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.example.databinding.AppActBinding
import kotlinx.coroutines.launch
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

    val okhttp = OkHttpClient.Builder().build()

    val retrofit: Api = Retrofit.Builder()
      .baseUrl("https://api.github.com/")
      .client(okhttp)
      .build()
      .create()

    binding.reload.setOnClickListener {
      lifecycleScope.launch {
        val r = retrofit.get()
        Log.d("hoge", r.raw().toString())
      }
    }
  }
}

interface Api {
  @GET("users/satoshun")
  suspend fun get(): Response<Unit>
}
