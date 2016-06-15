package and.universal.club.toggolino.de.toggolino.commons.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast

/**
 * Created by jakub on 09/05/16.
 */
inline fun FragmentActivity.setupFragment(@IdRes containerId: Int, addToBackstack: Boolean = false, fragment: () -> Fragment): Fragment {
  var fragment = supportFragmentManager.findFragmentById(containerId)
  if (fragment == null) {
    fragment = fragment()
    supportFragmentManager.beginTransaction().apply {
      add(containerId, fragment, if (addToBackstack) fragment.javaClass.simpleName else null)
      if (addToBackstack) addToBackStack(fragment.javaClass.simpleName)
    }.commit()
  }
  return fragment
}

inline fun FragmentActivity.setupFragmentOptional(@IdRes containerId: Int, addToBackstack: Boolean = false, fragment: () -> Fragment): Fragment? {
  return when (findViewById(containerId)) {
    null -> null
    else -> setupFragment(containerId, addToBackstack, fragment)
  }
}

inline fun <reified T : Activity> Activity.startActivity() = startActivity(Intent(this, T::class.java))
inline fun <reified T : Activity> Activity.startActivity(config: Bundle.() -> Unit) = startActivity(Intent(this, T::class.java).apply { putExtras(Bundle().apply { config() }) })
inline fun <reified T : Activity> Fragment.startActivity(): Unit = activity.startActivity<T>()
inline fun <reified T : Activity> android.app.Fragment.startActivity(): Unit = activity.startActivity<T>()

inline fun <reified T : Activity> Activity.startActivityWithTransitions(vararg sharedElements: Pair<View, String> = emptyArray()): Unit
    = startActivityWithTransitions<T>(*sharedElements) {}

inline fun <reified T : Activity> Activity.startActivityWithTransitions(vararg sharedElements: Pair<View, String> = emptyArray(),
                                                                        intentSetup: Intent.() -> Unit): Unit
    = startActivity(Intent(this, T::class.java).apply { intentSetup() }, makeSceneTransitionAnimation(*sharedElements).toBundle())

inline fun <reified T : Activity> Fragment.startActivityWithTransitions(vararg sharedElements: Pair<View, String> = emptyArray()): Unit
    = activity.startActivityWithTransitions<T>(*sharedElements) {}

inline fun <reified T : Activity> Fragment.startActivityWithTransitions(vararg sharedElements: Pair<View, String> = emptyArray(),
                                                                        intentSetup: Intent.() -> Unit): Unit
    = activity.startActivityWithTransitions<T>(*sharedElements) { intentSetup() }

inline fun <reified T : Activity> android.app.Fragment.startActivityWithTransitions(vararg sharedElements: Pair<View, String> = emptyArray()): Unit
    = activity.startActivityWithTransitions<T>(*sharedElements) {}

inline fun <reified T : Activity> android.app.Fragment.startActivityWithTransitions(vararg sharedElements: Pair<View, String> = emptyArray(),
                                                                                    intentSetup: Intent.() -> Unit): Unit
    = activity.startActivityWithTransitions<T>(*sharedElements) { intentSetup() }

fun Fragment.onClick(@IdRes id: Int, listener: (View) -> Unit) = view!!.findViewById(id)!!.setOnClickListener { listener(it) }

fun Activity.onClick(@IdRes id: Int, listener: (View) -> Unit) = findViewById(id)!!.setOnClickListener { listener(it) }

fun Fragment.onCheckedChanged(@IdRes id: Int, listener: (Boolean) -> Unit)
    = (view!!.findViewById(id)!! as CompoundButton).setOnCheckedChangeListener { button, isChecked -> listener(isChecked) }

fun Activity.onCheckedChanged(@IdRes id: Int, listener: (Boolean) -> Unit)
    = (findViewById(id)!! as CompoundButton).setOnCheckedChangeListener { button, isChecked -> listener(isChecked) }

fun Activity.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Fragment.toast(text: String) = Toast.makeText(activity, text, Toast.LENGTH_LONG).show()

fun Activity.makeSceneTransitionAnimation(vararg sharedElements: Pair<View, String> = emptyArray())
    = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *(sharedElements.supportPair))

private val Any?.unit: Unit get() = Unit

fun Activity.maybeInitImmersive(): Unit {
  var newUiOptions = getWindow().getDecorView().getSystemUiVisibility()

  // Navigation bar hiding:  Backwards compatible to ICS.
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
    newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
  }

  // Status bar hiding: Backwards compatible to Jellybean
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

  }
  getWindow().getDecorView().setSystemUiVisibility(newUiOptions)

}

fun Activity.maybeExitImmersive(): Unit {
  var newUiOptions = getWindow().getDecorView().getSystemUiVisibility()

  // Navigation bar hiding:  Backwards compatible to ICS.
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
    newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
  }

  // Status bar hiding: Backwards compatible to Jellybean
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()

  }
  getWindow().getDecorView().setSystemUiVisibility(newUiOptions)
}

/** Hides keyboard, needs a view to obtain window token */
fun Activity.hideKeyboard(view: View) = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)

/** Hides keyboard, needs a view to obtain window token */
fun Fragment.hideKeyboard(view: View) = activity?.hideKeyboard(view)

val <T, K> Pair<T, K>.supportPair: android.support.v4.util.Pair<T, K>
  get() = android.support.v4.util.Pair(first, second)

val <T, K> Array<out Pair<T, K>>.supportPair: Array<out android.support.v4.util.Pair<T, K>>
  get() = this.map { it.supportPair }.toTypedArray()
