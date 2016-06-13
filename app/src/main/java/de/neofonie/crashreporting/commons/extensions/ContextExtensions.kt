package and.universal.club.toggolino.de.toggolino.commons.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.support.annotation.ColorRes
import android.view.WindowManager

/**
 * Convenience extension methods for working with Context
 *
 * Created by dariusz on 25/04/16.
 */

fun Context.getScreenSize(): Point {
  val point = Point()
  (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
  return point
}

fun Context.getColorCompat(@ColorRes resId: Int, theme: Resources.Theme? = null) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      resources.getColor(resId, theme)
    } else {
      resources.getColor(resId)
    }