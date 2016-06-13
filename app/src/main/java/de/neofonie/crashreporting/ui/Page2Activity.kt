package de.neofonie.crashreporting.ui

import and.universal.club.toggolino.de.toggolino.commons.extensions.ioMain
import and.universal.club.toggolino.de.toggolino.utils.bindView
import android.widget.TextView
import de.neofonie.crashreporting.R
import de.neofonie.crashreporting.commons.BaseActivity
import rx.Observable
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit

/**
 * Created by jakub on 13/06/16.
 */
class Page2Activity : BaseActivity(R.layout.activity_page2) {

  private val countdownLabel: TextView by bindView(R.id.label_time_to_crash)

  private val countdownTime = 10

  private var countdownSubscription = Subscriptions.empty()
  private var timer = countdownTime

  override fun onResume() {
    super.onResume()
    countdownSubscription.unsubscribe()
    timer = countdownTime
    countdownSubscription = Observable.just(1L).mergeWith(Observable.interval(1, TimeUnit.SECONDS)).take(countdownTime).ioMain().subscribe ({ next ->
      countdownLabel.text = "${--timer}"
    }, {}, { // Completed
      throw RuntimeException("Timer crash!!!!")
    })
  }

  override fun onPause() {
    countdownSubscription.unsubscribe()
    super.onPause()
  }
}