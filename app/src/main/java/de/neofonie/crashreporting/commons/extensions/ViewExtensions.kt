package and.universal.club.toggolino.de.toggolino.commons.extensions

import android.os.Build
import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import rx.Observable

/**
 * Created by dariusz on 26/04/16.
 */

fun EditText.textChangesFromCurrent(): Observable<Boolean> {
  val referenceValue = this.text.toString()

  return Observable.create { subscriber ->
    addTextChangedListener(object : TextWatcher {
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        subscriber.onNext(referenceValue != s.toString())
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // do nothing
      }

      override fun afterTextChanged(s: Editable?) {
        // do nothing
      }
    })
  }
}

fun EditText.textAsString(): String = text.toString()

fun View.inflate(@LayoutRes resource: Int, root: ViewGroup?, attachToRoot: Boolean) = LayoutInflater.from(context).inflate(resource, root, attachToRoot)
fun android.app.Fragment.inflate(@LayoutRes resource: Int, root: ViewGroup?, attachToRoot: Boolean) = LayoutInflater.from(context).inflate(resource, root, attachToRoot)
fun android.support.v4.app.Fragment.inflate(@LayoutRes resource: Int, root: ViewGroup?, attachToRoot: Boolean) = LayoutInflater.from(context).inflate(resource, root, attachToRoot)

@IntDef(View.VISIBLE.toLong(), View.INVISIBLE.toLong(), View.GONE.toLong())
@Retention(AnnotationRetention.SOURCE)
annotation class Visibility

fun View.setVisibilityIfChanged(@Visibility visibility: Int) {
  if (this.visibility != visibility) this.visibility = visibility
}

fun TextView.setTextAppearanceCompat(@StyleRes resId: Int) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    setTextAppearance(resId)
  } else {
    setTextAppearance(context, resId)
  }
}