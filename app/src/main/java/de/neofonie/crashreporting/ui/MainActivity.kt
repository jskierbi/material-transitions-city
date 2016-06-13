package de.neofonie.crashreporting.ui

import and.universal.club.toggolino.de.toggolino.commons.extensions.onClick
import and.universal.club.toggolino.de.toggolino.commons.extensions.startActivity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import de.neofonie.crashreporting.R
import de.neofonie.crashreporting.commons.BaseActivity
import de.neofonie.crashreporting.ui.cities.ListActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    onClick(R.id.btn_force_crash) { throw RuntimeException("Forced crash!") }
    onClick(R.id.btn_log_non_fatal) { Crashlytics.log("This is main page non fatal execption") }
    onClick(R.id.btn_page_1) { startActivity<Page1Activity>() }
    onClick(R.id.btn_page_2) { startActivity<Page2Activity>() }
    onClick(R.id.btn_page_3) { startActivity<ListActivity>() }
  }
}
