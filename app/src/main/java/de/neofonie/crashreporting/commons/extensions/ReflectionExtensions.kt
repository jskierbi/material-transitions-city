package and.universal.club.toggolino.de.toggolino.commons.extensions

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * Created by marcinbak on 28/04/16.
 */
fun <T : Any> KClass<T>.easiestConstructor(): KFunction<T> {
  return constructors.firstOrDefault(
      {
        it.parameters.filter {
          it.type.toString().toLowerCase().contains("array")
        }.isEmpty()
      },
      {
        constructors.sortedBy { it.parameters.size }.first()
      }
  )
}