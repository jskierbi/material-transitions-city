package de.neofonie.crashreporting

import android.app.Activity
import android.app.Application
import com.crashlytics.android.Crashlytics
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.neofonie.crashreporting.modules.cities.api.CitiesApi
import io.fabric.sdk.android.Fabric
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by jakub on 13/06/16.
 */
class CrashApp : Application() {

  override fun onCreate() {
    super.onCreate()
    Fabric.with(this, Crashlytics());
  }

  val jackson = ObjectMapper().apply {
    registerKotlinModule()
    setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  }

  val retrofit = Retrofit.Builder().apply {
    baseUrl("https://api.myjson.com/")
    client(OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build())
    addConverterFactory(JacksonConverterFactory.create(jackson))
    addCallAdapterFactory(RxJavaCallAdapterFactory.create())
  }.build()

  val citiesApi = retrofit.create(CitiesApi::class.java)
}

val Activity.app: CrashApp get() = application as CrashApp