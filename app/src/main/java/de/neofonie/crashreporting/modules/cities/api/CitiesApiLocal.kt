package de.neofonie.crashreporting.modules.cities.api

import android.content.Context
import com.fasterxml.jackson.module.kotlin.readValue
import de.neofonie.crashreporting.app
import rx.Observable

/**
 * Created by q on 19/06/16.
 */
class CitiesApiLocal(val context: Context) : CitiesApi {
  override fun cityList() = Observable.just(context.app.jackson.readValue<List<CitiesApi.CityShort>>(context.assets.open("cities/citylist.json")))
  override fun cityDetails(id: String) = Observable.just(context.app.jackson.readValue<CitiesApi.CityDetails>(context.assets.open("cities/$id.json")))
}