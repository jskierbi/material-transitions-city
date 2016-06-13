package and.universal.club.toggolino.de.toggolino.commons.extensions

import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers.io

/**
 * Convenience extension methods for working with RxJava
 *
 * Created by marcinbak on 13/04/16.
 */
inline fun <reified T> singleOnSubscribe(crossinline body: () -> T): Observable<T> {
  return Observable.create<T> { subscriber ->
    try {
      subscriber.onNext(body())
      subscriber.onCompleted()
    } catch(e: Exception) {
      subscriber.onError(e)
    }
  }
}

fun <T> Observable<T>.subscribeIoObserveMain(): Observable<T> {
  return this.subscribeOn(io()).observeOn(mainThread())
}

fun <T> Observable<T>.ioMain() = subscribeOn(io()).observeOn(mainThread())