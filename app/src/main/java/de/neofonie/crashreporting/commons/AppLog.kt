package de.neofonie.crashreporting.commons

import android.util.Log
//import com.crashlytics.android.Crashlytics
//import com.google.firebase.crash.FirebaseCrash

/**
 * Created by jakub on 14/06/16.
 */
class AppLog {
  companion object {
    fun d(tag: String, msg: String) = Log.d(tag, msg)

    fun e(tag: String, msg: String, error: Throwable? = null) {

      when (error) {
        null -> Log.e(tag, msg)
        else -> Log.e(tag, msg, error)
      }
//      Crashlytics.log(Log.ERROR, tag, msg)
//      FirebaseCrash.logcat(Log.ERROR, tag, msg)
//      FirebaseCrash.report(error ?: RuntimeException(msg))
    }

    fun w(tag: String, msg: String, error: Throwable? = null) {
      when (error) {
        null -> Log.w(tag, msg)
        else -> Log.w(tag, msg, error)
      }
//      Crashlytics.log(Log.WARN, tag, msg)
//      FirebaseCrash.logcat(Log.WARN, tag, msg)
//      FirebaseCrash.report(NonFatalException())
    }
  }

  private class NonFatalException : Exception("Non Fatal Exception")
}