package and.universal.club.toggolino.de.toggolino.commons.extensions

import java.io.File

/**
 * Created by marcinbak on 26/04/16.
 */

fun File.walkFileTree(action: (file: File) -> Unit) {
  if (isDirectory) {
    listFiles().forEach { file -> file.walkFileTree(action) }
  }
  action(this)
}