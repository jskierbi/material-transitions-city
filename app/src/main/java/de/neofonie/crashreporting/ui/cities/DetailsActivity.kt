package de.neofonie.crashreporting.ui.cities

import and.universal.club.toggolino.de.toggolino.commons.extensions.extra
import and.universal.club.toggolino.de.toggolino.commons.extensions.toast
import android.os.Bundle
import de.neofonie.crashreporting.R
import de.neofonie.crashreporting.commons.BaseActivity

/**
 * Created by jakub on 13/06/16.
 */
class DetailsActivity : BaseActivity(R.layout.network_details_activity) {

  companion object {
    val EXTRA_CITY_ID = "EXTRA_CITY_ID"
  }

  val cityId: String by extra(EXTRA_CITY_ID)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    toast("City: $cityId")
  }
}