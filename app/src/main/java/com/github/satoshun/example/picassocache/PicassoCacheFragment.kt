package com.github.satoshun.example.picassocache

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.example.databinding.PicassoCacheFragBinding
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.system.measureTimeMillis

class PicassoCacheFragment : Fragment() {
  private lateinit var binding: PicassoCacheFragBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = PicassoCacheFragBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    run {
      val logging = HttpLoggingInterceptor()
      logging.level = HttpLoggingInterceptor.Level.HEADERS

      val okhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

      val picasso = Picasso.Builder(requireContext())
        .downloader(OkHttp3Downloader(okhttp))
        .build()

      binding.noCache.setOnClickListener {
        lifecycleScope.launch {
          withContext(Dispatchers.IO) {
            val time = measureTimeMillis {
              val bitmap = picasso
                .load("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png")
                .get()
            }
            Log.d("no cache", time.toString())
          }
        }
      }
    }

    run {
      val logging = HttpLoggingInterceptor()
      logging.level = HttpLoggingInterceptor.Level.HEADERS

      val okhttp = OkHttpClient.Builder()
        .cache(Cache(requireActivity().cacheDir, (10 * 1024 * 1024).toLong()))
        .addInterceptor(logging)
        .build()

      val picasso = Picasso.Builder(requireContext())
        .downloader(OkHttp3Downloader(okhttp))
        .memoryCache(LruCache(requireContext()))
        .build()

      binding.cache.setOnClickListener {
        lifecycleScope.launch {
          withContext(Dispatchers.IO) {
            val time = measureTimeMillis {
              val bitmap = picasso
                .load("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png")
                .get()
            }
            Log.d("cache", time.toString())
          }
        }
      }
    }
  }
}
