package org.raml.parser


import java.io.Reader

import org.raml.domain.Api
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor
import org.yaml.snakeyaml.nodes.{MappingNode, Node}

import scala.collection.JavaConverters._

import java.util.{Map ⇒ JMap}

/**
  * We parse using SnakeYaml events.
  *
  * Yaml is represented as a DAG: http://yaml.org/spec/1.1/#id861060
  *
  * {{{
  * <org.yaml.snakeyaml.events.StreamStartEvent()>
  * <org.yaml.snakeyaml.events.DocumentStartEvent()>
  * <org.yaml.snakeyaml.events.MappingStartEvent(anchor=null, tag=null, implicit=true)>
  * <org.yaml.snakeyaml.events.ScalarEvent(anchor=null, tag=null, implicit=[true, false], value=title)>
  * <org.yaml.snakeyaml.events.ScalarEvent(anchor=null, tag=null, implicit=[true, false], value=GitHub API)>
  * <org.yaml.snakeyaml.events.ScalarEvent(anchor=null, tag=null, implicit=[true, false], value=version)>
  * <org.yaml.snakeyaml.events.ScalarEvent(anchor=null, tag=null, implicit=[true, false], value=v3)>
  * <org.yaml.snakeyaml.events.ScalarEvent(anchor=null, tag=null, implicit=[true, false], value=baseUri)>
  * <org.yaml.snakeyaml.events.ScalarEvent(anchor=null, tag=null, implicit=[true, false], value=https://api.github.com)>
  * <org.yaml.snakeyaml.events.MappingEndEvent()>
  * <org.yaml.snakeyaml.events.DocumentEndEvent()>
  * <org.yaml.snakeyaml.events.StreamEndEvent()>
  * }}}
  */
class Ctor extends SafeConstructor {
  def construct(node: Node): Any = {
    constructObject(node)
  }

}

class RamlParser extends Yaml(new Ctor()) {
  val ctor = constructor.asInstanceOf[Ctor]
  def construct(node: Node): Any = ctor.construct(node)

  def apply(reader: Reader): Unit = {
    val root: Node = compose(reader)
    val mappingNode = root.asInstanceOf[MappingNode]
    val map: Map[String, Any] = construct(mappingNode).asInstanceOf[JMap[String, Any]].asScala
  }
}
