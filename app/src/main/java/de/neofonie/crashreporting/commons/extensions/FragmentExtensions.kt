package and.universal.club.toggolino.de.toggolino.commons.extensions

import android.app.Fragment
import android.support.v4.app.Fragment as SupportFragment;

/**
 * Created by mdabrowski on 12/05/16.
 */

fun SupportFragment.clearFocus() {
  activity?.currentFocus?.clearFocus()
}

fun Fragment.clearFocus() {
  activity?.currentFocus?.clearFocus()
}