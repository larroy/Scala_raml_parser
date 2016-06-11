package org.raml.parser

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, ValidatedNel}
import org.raml.domain.Api
import org.yaml.snakeyaml.ObjectAndNodes
import org.yaml.snakeyaml.nodes.Node
import java.util.{Map ⇒ JMap}

object ApiParser {
  def apply(objectAndNodes: ObjectAndNodes): ValidatedNel[ParserError, Api]= {
    val root = objectAndNodes.`object`.asInstanceOf[JMap[String, AnyRef]]
    var parsingErrors = List.empty[ParserError]
    val title = root.get("title")
    var api = Api("")
    if (title == null)
      parsingErrors = ParserError("title element for the API is mandatory", Option(objectAndNodes.object2node.get(root))) :: parsingErrors
    else
      api = api.copy(title = title)

    Option(root.get("description").asInstanceOf[String]).map { x ⇒ api = api.copy(description = x) }
    Option(root.get("version").asInstanceOf[String]).map { x ⇒ api = api.copy(version = x) }
    Option(root.get("baseUri").asInstanceOf[String]).map { x ⇒ api = api.copy(baseUri = x) }




    if (parsingErrors.nonEmpty)
      Invalid(NonEmptyList(parsingErrors.head, parsingErrors.tail))
    else
      Valid(api)
  }
}
