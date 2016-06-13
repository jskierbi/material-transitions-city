package and.universal.club.toggolino.de.toggolino.commons.extensions

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by marcinbak on 04/05/16.
 */
fun <A : Any> Fragment.arg(key: String, default: A? = null)
    : ReadWriteProperty<Fragment, A> = required(key, { arguments }, default)

fun <A : Any> android.support.v4.app.Fragment.arg(key: String, default: A? = null)
    : ReadWriteProperty<android.support.v4.app.Fragment, A> = required(key, { arguments }, default)

fun <A : Any?> Fragment.argOptional(key: String, default: A? = null)
    : ReadWriteProperty<Fragment, A?> = optional(key, { arguments }, default)

fun <A : Any?> android.support.v4.app.Fragment.argOptional(key: String, default: A? = null)
    : ReadWriteProperty<android.support.v4.app.Fragment, A?> = optional(key, { arguments }, default)

fun <A> Activity.extra(key: String, default: A? = null)
    : ReadWriteProperty<Activity, A> = required(key, { intent.extras }, default)

fun <A> Activity.extraOptional(key: String, default: A? = null)
    : ReadWriteProperty<Activity, A?> = optional(key, { intent.extras }, default)

fun <T : android.support.v4.app.Fragment> T.with(vararg args: Pair<String, Any?> = emptyArray()): T {
  val bundle = arguments ?: Bundle()
  arguments = bundle
  bundle.fillWith(args)
  return this
}

fun <T : Fragment> T.with(vararg args: Pair<String, Any?> = emptyArray()): T {
  val bundle = arguments ?: Bundle()
  arguments = bundle
  bundle.fillWith(args)
  return this
}

fun Intent.putExtras(vararg extras: Pair<String, Any?>) {
  val bundle = getExtras() ?: Bundle().apply { putExtras(this) }
  bundle.fillWith(extras)
}

private fun Bundle.fillWith(extras: Array<out Pair<String, Any?>> = emptyArray()) {
  for ((key, value) in extras) {
    when (value) {
      is Boolean -> putBoolean(key, value)
      is BooleanArray -> putBooleanArray(key, value)
      is Byte -> putByte(key, value)
      is ByteArray -> putByteArray(key, value)
      is Char -> putChar(key, value)
      is CharArray -> putCharArray(key, value)
      is CharSequence -> putCharSequence(key, value)
      is Double -> putDouble(key, value)
      is DoubleArray -> putDoubleArray(key, value)
      is Float -> putFloat(key, value)
      is FloatArray -> putFloatArray(key, value)
      is Int -> putInt(key, value)
      is IntArray -> putIntArray(key, value)
      is Long -> putLong(key, value)
      is LongArray -> putLongArray(key, value)
      is Short -> putShort(key, value)
      is ShortArray -> putShortArray(key, value)
    //      is Size -> putSize(key, value)
    //      is SizeF -> putSizeF(key, value)
      is String -> putString(key, value)
      is Parcelable -> putParcelable(key, value)
      is Serializable -> putSerializable(key, value)
      null -> {
      } // nothing
      else -> {
        throw IllegalArgumentException("$key cannot be put in bundle.")
      }
    }
  }
}

@Suppress("UNCHECKED_CAST")
private fun <T, A> required(key: String, bundle: () -> Bundle?, default: A?)
    = Lazy { t: T, desc -> (bundle()?.get(key) as A) ?: default ?: argumentNotFound(desc) }

@Suppress("UNCHECKED_CAST")
private fun <T, A> optional(key: String, bundle: () -> Bundle?, default: A?)
    = OptionalLazy { t: T, desc -> bundle()?.get(key) as A ?: default }

private fun argumentNotFound(desc: KProperty<*>): Nothing =
    throw IllegalStateException("Argument for '${desc.name}' not found.")

private class Lazy<T, A>(private val initializer: (T, KProperty<*>) -> A?) : ReadWriteProperty<T, A> {

  override fun setValue(thisRef: T, property: KProperty<*>, value: A) {
    this.value = value
  }

  private object EMPTY

  private var value: Any? = EMPTY

  override fun getValue(thisRef: T, property: KProperty<*>): A {
    if (value == EMPTY) {
      value = initializer(thisRef, property)
    }
    @Suppress("UNCHECKED_CAST")
    return value as A
  }
}

private class OptionalLazy<T, A>(private val initializer: (T, KProperty<*>) -> A?) : ReadWriteProperty<T, A?> {

  override fun setValue(thisRef: T, property: KProperty<*>, value: A?) {
    this.value = value
  }

  private object EMPTY

  private var value: Any? = EMPTY

  override fun getValue(thisRef: T, property: KProperty<*>): A? {
    if (value == EMPTY) {
      value = initializer(thisRef, property)
    }
    @Suppress("UNCHECKED_CAST")
    return value as A
  }
}