package de.neofonie.crashreporting.ui.cities

import and.universal.club.toggolino.de.toggolino.commons.extensions.extra
import and.universal.club.toggolino.de.toggolino.commons.extensions.ioMain
import and.universal.club.toggolino.de.toggolino.commons.extensions.onClick
import and.universal.club.toggolino.de.toggolino.commons.extensions.toast
import and.universal.club.toggolino.de.toggolino.utils.bindView
import android.animation.Animator
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import de.neofonie.crashreporting.R
import de.neofonie.crashreporting.app
import de.neofonie.crashreporting.commons.BaseActivity
import de.neofonie.crashreporting.commons.LoadingLayout
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
  val title: TextView by bindView(R.id.title)
  val desc: TextView by bindView(R.id.description)
  val fab: View by bindView(R.id.fab)
  val fabContainer: ViewGroup by bindView(R.id.fab_container)

  var loadSubscription = Subscriptions.empty()
  var dialogSubscription = Subscriptions.empty()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    image.setImageResource(when (cityId) {
      "26tq8" -> R.drawable.berlin
      "28yw0" -> R.drawable.warsaw
      "18qvk" -> R.drawable.newyork
      else -> R.drawable.transparent
    })
    setupTransitions()
    onClick(R.id.fab) { toast("Not implemented yet") }
    subscribeLoadNetwork()

    if (savedInstanceState == null) {
      fab.scaleX = 0f;
      fab.scaleY = 0f;
      window.enterTransition.addListener(object : Transition.TransitionListener {
        override fun onTransitionCancel(transition: Transition?) = Unit
        override fun onTransitionEnd(transition: Transition?) {
          window.enterTransition.removeListener(this);
          fab.animate().scaleX(1f).scaleY(1f);
        }

        override fun onTransitionPause(transition: Transition?) = Unit
        override fun onTransitionResume(transition: Transition?) = Unit
        override fun onTransitionStart(transition: Transition?) = Unit
      })
    }
  }

  override fun onDestroy() {
    loadSubscription.unsubscribe()
    dialogSubscription.unsubscribe()
    super.onDestroy()
  }

  override fun onBackPressed() {
    fab.animate().scaleX(0f).scaleY(0f).setListener(object : Animator.AnimatorListener {
      override fun onAnimationRepeat(animation: Animator?) = Unit
      override fun onAnimationCancel(animation: Animator?) = Unit
      override fun onAnimationStart(animation: Animator?) = Unit
      override fun onAnimationEnd(animation: Animator?) {
        supportFinishAfterTransition()
      }
    })
  }

  fun subscribeLoadNetwork() {
    loadingLayout.isLoadingVisible = true
    loadSubscription.unsubscribe()
    loadSubscription = app.citiesApi.cityDetails(cityId).ioMain()
        .doOnUnsubscribe { loadingLayout.isLoadingVisible = false }
        .subscribe({ next ->
          uiBindData(next)
        }, { error ->
          Log.e("TAG", "Network error", error)
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
//    Picasso.with(this).load(data.img).fit().centerCrop().into(image)
    title.text = data.name
    desc.text = data.description
  }

  private fun setupTransitions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      fabContainer.isTransitionGroup = true
      image.transitionName = getString(R.string.transition_image)

      window.sharedElementEnterTransition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion)

      window.enterTransition = TransitionSet()
          .addTransition(Slide(Gravity.BOTTOM).addTarget(R.id.description))
//          .addTransition(Slide(Gravity.RIGHT).addTarget(R.id.fab_container))
          .addTransition(Fade(Fade.IN).addTarget(R.id.title))
          .excludeTarget(android.R.id.statusBarBackground, true)
          .excludeTarget(android.R.id.navigationBarBackground, true)

//      window.returnTransition = TransitionSet()

    }
  }
}