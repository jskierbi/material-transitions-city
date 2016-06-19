package de.neofonie.crashreporting.ui.cities

import and.universal.club.toggolino.de.toggolino.commons.extensions.extra
import and.universal.club.toggolino.de.toggolino.commons.extensions.ioMain
import and.universal.club.toggolino.de.toggolino.commons.extensions.onClick
import and.universal.club.toggolino.de.toggolino.commons.extensions.toast
import and.universal.club.toggolino.de.toggolino.utils.bindView
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.Fade
import android.transition.TransitionSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fasterxml.jackson.databind.JsonMappingException
import com.squareup.picasso.Picasso
import de.neofonie.crashreporting.R
import de.neofonie.crashreporting.app
import de.neofonie.crashreporting.commons.AppLog
import de.neofonie.crashreporting.commons.BaseActivity
import de.neofonie.crashreporting.commons.LoadingLayout
import de.neofonie.crashreporting.commons.transitions.RevealTransition
import de.neofonie.crashreporting.commons.transitions.ZoomTransition
import de.neofonie.crashreporting.modules.cities.api.CitiesApi
import rx.subscriptions.Subscriptions

/**
 * Created by jakub on 13/06/16.
 */
class DetailsActivity : BaseActivity(R.layout.network_details_activity) {

  companion object {
    val EXTRA_CITY_ID = "EXTRA_CITY_ID"
  }

  val cityId: String by extra(EXTRA_CITY_ID)

  val loadingLayout: LoadingLayout by bindView(R.id.loading_layout)
  val image: ImageView by bindView(R.id.image)
  val thumb: ImageView by bindView(R.id.thumb)
  val title: TextView by bindView(R.id.title)
  val desc: TextView by bindView(R.id.description)
  val fab: View by bindView(R.id.fab)
  val fabContainer: ViewGroup by bindView(R.id.fab_container)

  var loadSubscription = Subscriptions.empty()
  var dialogSubscription = Subscriptions.empty()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setupTransitions()
    onClick(R.id.fab) { toast("Not implemented yet") }
    subscribeLoadNetwork()
  }

  override fun onDestroy() {
    loadSubscription.unsubscribe()
    dialogSubscription.unsubscribe()
    super.onDestroy()
  }

  fun subscribeLoadNetwork() {
    loadingLayout.isLoadingVisible = true
    loadSubscription.unsubscribe()
    loadSubscription = app.citiesApi.cityDetails(cityId).ioMain()
        .doOnUnsubscribe { loadingLayout.isLoadingVisible = false }
        .subscribe({ next ->
          uiBindData(next)
        }, { error ->
          when (error) {
            is JsonMappingException -> AppLog.e("TAG", "Invalid Server Data Error", error)
            else -> AppLog.w("TAG", "Network error", error)
          }
          val dialog = AlertDialog.Builder(this).apply {
            setTitle("Network failed")
            setMessage("Retry?")
            setPositiveButton("Retry") { d, w -> subscribeLoadNetwork() }
            setNegativeButton("Back") { d, v -> onBackPressed() }
            setOnCancelListener { onBackPressed() }
          }.show()
          dialogSubscription.unsubscribe()
          dialogSubscription = Subscriptions.create { dialog.dismiss() }
        })
  }

  fun uiBindData(data: CitiesApi.CityDetails) {
    title.text = data.name
    desc.text = data.description
    Picasso.with(this).load(data.img).into(image)
    Picasso.with(this).load(data.thumb).into(thumb)
  }

  private fun setupTransitions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      fabContainer.isTransitionGroup = true
//      window.sharedElementsUseOverlay = false

      thumb.transitionName = getString(R.string.transition_image)

      window.sharedElementReturnTransition = TransitionSet().apply {
        addTransition(ChangeImageTransform())
        addTransition(ChangeBounds())
        startDelay = 200L
      }

      window.enterTransition = TransitionSet().apply {
        addTransition(Fade(Fade.IN).addTarget(R.id.title).addTarget(R.id.description).setStartDelay(200L))
        addTransition(RevealTransition().addTarget(R.id.image).setStartDelay(200L))
        addTransition(ZoomTransition().addTarget(R.id.fab_container))
        excludeTarget(android.R.id.statusBarBackground, true)
        excludeTarget(android.R.id.navigationBarBackground, true)
      }

      window.returnTransition = TransitionSet().apply {
        addTransition(Fade(Fade.OUT).addTarget(R.id.title).addTarget(R.id.description))
        addTransition(RevealTransition().addTarget(R.id.image))
        addTransition(ZoomTransition().addTarget(R.id.fab_container))
        excludeTarget(android.R.id.statusBarBackground, true)
        excludeTarget(android.R.id.navigationBarBackground, true)
      }

    }
  }
}