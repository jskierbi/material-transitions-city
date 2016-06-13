package and.universal.club.toggolino.de.toggolino.commons.extensions

/**
 * Created by marcinbak on 27/04/16.
 */
inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
  var sum: Long = 0
  for (element in this) {
    sum += selector(element)
  }
  return sum
}

fun <T> Collection<T>.firstOrDefault(predicate: (T) -> Boolean, default: () -> T): T {
  return firstOrNull(predicate) ?: default()
}