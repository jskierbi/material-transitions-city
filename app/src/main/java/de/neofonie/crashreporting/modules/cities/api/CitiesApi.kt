package de.neofonie.crashreporting.modules.cities.api

import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by jakub on 13/06/16.
 */
interface CitiesApi {

  @GET("/bins/qp9g")
  fun cityList(): Observable<List<CityShort>>

  @GET("/bins/{id}")
  fun cityDetails(@Path("id") id: String): Observable<CityDetails>


  data class CityShort(val name: String,
                       val thumb: String,
                       val details_id: String)

  data class CityDetails(val name: String,
                         val description: String,
                         val thumb: String,
                         val img: String)
}