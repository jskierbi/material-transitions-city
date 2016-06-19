package de.neofonie.crashreporting

import android.app.Activity
import android.app.Application
import android.content.Context
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.neofonie.crashreporting.modules.cities.api.CitiesApiLocal
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by jakub on 13/06/16.
 */
class TransitionsApp : Application() {

  val jackson = ObjectMapper().apply {
    registerKotlinModule()
    setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
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

  val citiesApi =
      CitiesApiLocal(this)
//      retrofit.create(CitiesApi::class.java)
}

val Context.app: TransitionsApp get() = applicationContext as TransitionsApp
val Activity.app: TransitionsApp get() = application as TransitionsApp