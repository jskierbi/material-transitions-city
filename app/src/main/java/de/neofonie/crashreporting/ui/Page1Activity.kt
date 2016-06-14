package de.neofonie.crashreporting.ui

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.google.firebase.crash.FirebaseCrash
import de.neofonie.crashreporting.R
import de.neofonie.crashreporting.commons.BaseActivity

/**
 * Created by jakub on 13/06/16.
 */
class Page1Activity : BaseActivity(R.layout.activity_page1) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Crashlytics.logException(IllegalStateException("User tried to access Page1 (and he probably shouldn't have an access)"))
    FirebaseCrash.report(IllegalStateException("User tried to access Page1 (and he probably shouldn't have an access)"))
  }
}