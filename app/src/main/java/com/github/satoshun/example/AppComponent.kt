package com.github.satoshun.example

import android.app.Application
import android.os.Looper
import dagger.BindsInstance
import dagger.Component
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
  fun inject(activity: AppActivity)

  @Component.Factory
  interface Builder {
    fun create(@BindsInstance application: Application): AppComponent
  }
}

@Module
class AppModule {
  @Provides
  @Singleton
  fun provideCache(context: Application): Cache {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      throw IllegalStateException("Cache initialized on main thread.")
    }
    return Cache(context.cacheDir, (10 * 1024 * 1024).toLong())
  }

  @Singleton
  @Provides
  fun provideOkHttpClient(cache: Cache): OkHttpClient {
    return OkHttpClient.Builder().cache(cache).build()
  }

  @Provides
  fun provideGitHubApi(
    client: Lazy<OkHttpClient>
  ): GitHubApi {
    return Retrofit.Builder()
      .callFactory(object : Call.Factory {
        override fun newCall(request: Request): Call {
          println("OKHttp------")
          println(Looper.myLooper() == Looper.getMainLooper())
          return client.get().newCall(request)
        }
      })
      .baseUrl("https://hoge.com")
      .build()
      .create()
  }
}

interface GitHubApi {
  @GET("hoge")
  suspend fun test()

  @GET("hoge")
  fun test2(): retrofit2.Call<Unit>
}
