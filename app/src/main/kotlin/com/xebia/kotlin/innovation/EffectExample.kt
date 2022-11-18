package com.xebia.kotlin.innovation

import arrow.core.continuations.Effect
import arrow.core.continuations.effect
import arrow.core.continuations.ensureNotNull
import java.io.File
import java.io.FileNotFoundException

@JvmInline
value class Content(val body: List<String>)
sealed interface FileError

@JvmInline
value class SecurityError(val msg: String?) : FileError

@JvmInline
value class FileNotFound(val path: String) : FileError
object EmptyPath : FileError {
  override fun toString() = "EmptyPath"
}

fun readFile(path: String?): Effect<FileError, Content> = effect {
  ensureNotNull(path) { EmptyPath }
  ensure(path.isNotEmpty()) { EmptyPath }
  try {
    val lines = File("${System.getProperty("user.dir")}/$path").readLines()
    Content(lines)
  } catch (e: FileNotFoundException) {
    shift(FileNotFound(path))
  } catch (e: SecurityException) {
    shift(SecurityError(e.message))
  }
}
