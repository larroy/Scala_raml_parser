package org.raml.parser

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import org.raml.domain.{Api, Method, Resource}
import org.yaml.snakeyaml.ObjectAndNodes
import java.util.{Map ⇒ JMap}

import scala.collection.mutable
import scala.collection.immutable.IndexedSeq
import scala.reflect.ClassTag

//import cats._
//import cats.syntax.eq._
//import cats.std.all.Eq
//import cats.syntax.eq._
import scala.collection.JavaConverters._
import org.raml.utils.AsInstanceOfOption._

class ApiParser(objectAndNodes: ObjectAndNodes) {
  type MapT = mutable.Map[String, AnyRef]
  type JMapT = JMap[String, AnyRef]
  private[this] val root: MapT = objectAndNodes.`object`.asInstanceOf[JMap[String, AnyRef]].asScala
  private[this] val object2node = objectAndNodes.object2node.asScala
  private[this] val MethodNames = Set("get", "patch", "put", "post", "delete", "options", "head")
  private[this] var parsingErrors = List.empty[ParserError]
  private[this] var api = Api("")

  def apply(): ValidatedNel[ParserError, Api] = {
    parse[String](root, "title", { s ⇒ api = api.copy(title = s) })
    parseOptional[String](root, "description", { s ⇒ api = api.copy(description = s) })
    parseOptional[String](root, "version", { s ⇒ api = api.copy(description = s) })
    parseOptional[String](root, "baseUri", { s ⇒ api = api.copy(description = s) })

    api = api.copy(resources = parseMatching(root, isResourceKey, parseResource))

    if (parsingErrors.nonEmpty)
      Invalid(NonEmptyList(parsingErrors.head, parsingErrors.tail))
    else
      Valid(api)
  }

  /**
    * @return true if the key identifies a resource, starts with '/'
    */
  def isResourceKey(key: String): Boolean =
    key.nonEmpty && key.head == '/'

  def parseResource(resourceMap: MapT): Resource = {
    var resource = Resource()
    parseOptional[String](resourceMap, "displayName", { s ⇒ resource = resource.copy(displayName = s) })
    parseOptional[String](resourceMap, "description", { s ⇒ resource = resource.copy(description = s) })
    // TODO annotations

    resource = resource.copy(methods = parseMatching(resourceMap, isMethodKey, parseMethod))
    resource
  }

  /**
    * Applies parseF to elements from xs to which keyMatch returns true and return a sequence of its results
    */
  def parseMatching[T](xs: MapT, keyMatch: (String) ⇒ Boolean, parseF: (MapT) ⇒ T): IndexedSeq[T] = {
    xs.keys.filter(keyMatch).flatMap { key ⇒
      xs(key).asInstanceOfOption[JMapT] match {
        case Some(mapAtKey) ⇒
          Some(parseF(mapAtKey.asScala))
        case None ⇒
          parsingErrors = parsingErrors :+ ParserError(s"Expected a mapping at: $key", object2node.get(xs(key)))
          None
      }
    }.toIndexedSeq
  }

  def isMethodKey(key: String): Boolean =
    MethodNames.contains(key.toLowerCase)

  def parseMethod(methodMap: MapT): Method = {
    var method = Method()
    parseOptional[String](methodMap, "displayName", { s ⇒ method = method.copy(displayName = s) })
    parseOptional[String](methodMap, "description", { s ⇒ method = method.copy(description = s) })
    // TODO
    method
  }

  /**
    * Look for an optional key, and run the side effect f when found and the type matches
    */
  def parseOptional[T](xs: MapT, key: String, f: T ⇒ Unit): Unit = {
    xs.get(key).foreach { value ⇒
      try {
        f(value.asInstanceOf[T])
      } catch {
        case e: ClassCastException ⇒
          parsingErrors = parsingErrors :+ ParserError(e.toString, object2node.get(value))
      }
    }
  }

  /**
    * Look for a mandatory key, and run the side effect f when found and the type matches, otherwise is a parsing error
    */
  def parse[T](xs: MapT, key: String, f: T ⇒ Unit): Unit = {
    if (!xs.contains(key)) {
      parsingErrors = parsingErrors :+ ParserError(s"key not found: $key")
      return
    }
    val value = xs(key)
    try {
      f(value.asInstanceOf[T])
    } catch {
      case e: ClassCastException ⇒
        parsingErrors = parsingErrors :+ ParserError(e.toString, object2node.get(value))
    }
  }
}
