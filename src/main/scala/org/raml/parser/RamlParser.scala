package org.raml.parser


import java.io.Reader

import org.raml.domain.Api
import org.yaml.snakeyaml.{ObjectAndNodes, Yaml}
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

class RamlParser {
  def apply(content: String): Unit = {
    val yaml = new Yaml(SafeConstructor())
    val objectAndNodes: ObjectAndNodes = yaml.loadWithNodes(content)
  }
}
