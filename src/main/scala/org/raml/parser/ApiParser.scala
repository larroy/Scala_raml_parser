package org.raml.parser

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import org.raml.domain.{Api, Method, Resource}
import org.yaml.snakeyaml.ObjectAndNodes
import java.util.{Map ⇒ JMap}

import scala.collection.mutable

import scala.collection.immutable.IndexedSeq
//import cats._
//import cats.syntax.eq._
//import cats.std.all.Eq
//import cats.syntax.eq._
import scala.collection.JavaConverters._
import org.raml.utils.AsInstanceOfOption._

class ApiParser(objectAndNodes: ObjectAndNodes) {
  type MapT = mutable.Map[String, AnyRef]
  type JMapT = JMap[String, AnyRef]
  val root: MapT = objectAndNodes.`object`.asInstanceOf[JMap[String, AnyRef]].asScala
  val object2node = objectAndNodes.object2node.asScala
  var parsingErrors = List.empty[ParserError]
  var api = Api("")
  val MethodNames = Set("get", "patch", "put", "post", "delete", "options", "head")

  def parseResources(xs: MapT): IndexedSeq[Resource] = {
    xs.keys.filter(isResourceKey).flatMap { key ⇒
      xs(key).asInstanceOfOption[JMapT] match {
        case Some(resourceMap) ⇒
          Some(parseResource(resourceMap.asScala, key))
        case None ⇒
          parsingErrors :+ ParserError(s"Expected a mapping at: $key", object2node.get(xs(key)))
          None
      }
    }.toIndexedSeq
  }

  /*
  def parseMatching[T](xs: MapT, keyMatch ⇒ Boolean, f: (MapT) ⇒ T): T = {

  }
  */

  def isResourceKey(key: String): Boolean =
    key.nonEmpty && key.head == '/'

  def parseResource(resourceMap: MapT, key: String): Resource = {
    var resource = Resource()
    parseOptional[String](resourceMap, "displayName", { s ⇒ resource = resource.copy(displayName = s) })
    parseOptional[String](resourceMap, "description", { s ⇒ resource = resource.copy(description = s) })
    // TODO annotations

    resource = resource.copy(methods = parseMethods(resourceMap))
    resource
  }

  def isMethodKey(key: String): Boolean =
    MethodNames.contains(key.toLowerCase)

  def parseMethods(xs: MapT): IndexedSeq[Method] = {
    xs.keys.filter(isMethodKey).flatMap { key ⇒
        xs(key).asInstanceOfOption[JMapT] match {
        case Some(resourceMap) ⇒
          Some(parseMethod(resourceMap.asScala, key))
        case None ⇒
          parsingErrors :+ ParserError(s"Expected a mapping at: $key", object2node.get(xs(key)))
          None
      }
    }.toIndexedSeq
    IndexedSeq.empty[Method]
  }

  def parseMethod(methodMap: MapT, key: String): Method = {
    var method = Method()
    // FIXME
    method
  }


  def apply(): ValidatedNel[ParserError, Api] = {
    parse[String](root, "title", { s ⇒ api = api.copy(title = s) })
    parseOptional[String](root, "description", { s ⇒ api = api.copy(description = s) })
    parseOptional[String](root, "version", { s ⇒ api = api.copy(description = s) })
    parseOptional[String](root, "baseUri", { s ⇒ api = api.copy(description = s) })

    api = api.copy(resources = parseResources(root))

    if (parsingErrors.nonEmpty)
      Invalid(NonEmptyList(parsingErrors.head, parsingErrors.tail))
    else
      Valid(api)
  }

  def parseOptional[T](xs: MapT, key: String, f: T ⇒ Unit): Unit = {
    if (!xs.contains(key))
      return
    val value = xs(key)
    try {
      f(value.asInstanceOf[T])
    } catch {
      case e: ClassCastException ⇒
        parsingErrors :+ ParserError(e.toString, object2node.get(value))
    }
  }

  def parse[T](xs: MapT, key: String, f: T ⇒ Unit): Unit = {
    if (!xs.contains(key)) {
      parsingErrors :+ ParserError(s"key not found: $key")
      return
    }
    val value = xs(key)
    try {
      f(value.asInstanceOf[T])
    } catch {
      case e: ClassCastException ⇒
        parsingErrors :+ ParserError(e.toString, object2node.get(value))
    }
  }
}
