package org.raml.utils

import scala.reflect.ClassTag

object AsInstanceOfOption {
  implicit class AsInstanceOfOptionConversion(val x: Any) extends AnyVal {
    def asInstanceOfOption[T: ClassTag]: Option[T] = {
      Some(x) collect { case m: T => m }
    }
  }
}
