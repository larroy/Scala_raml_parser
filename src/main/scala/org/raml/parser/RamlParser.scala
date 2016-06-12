package org.raml.parser


import java.io.Reader

import org.raml.domain.Api
import org.yaml.snakeyaml.{ObjectAndNodes, Yaml}
import org.yaml.snakeyaml.constructor.SafeConstructor
import org.yaml.snakeyaml.nodes.{MappingNode, Node}

import scala.collection.JavaConverters._
import java.util.{Map ⇒ JMap}

import cats.data.ValidatedNel

/**
  * Yaml is represented as a DAG: http://yaml.org/spec/1.1/#id861060
  *
  */
class RamlParser {
  def apply(content: String): ValidatedNel[ParserError, Api] = {
    val yaml = new Yaml(new SafeConstructor())
    /*
    After parsing we get java Objects corresponding to the Yaml Nodes such as ArrayList for Seq, HashMap for Map and
    POD Java types for scalar nodes.

    We also have the SnakeYaml Nodes to keep a reference to the position in the file and the base Yaml tree.
     */
    val objectAndNodes: ObjectAndNodes = yaml.loadWithNodes(content)
    val validApi: ValidatedNel[ParserError, Api] = new ApiParser(objectAndNodes)()
    validApi
  }
}
