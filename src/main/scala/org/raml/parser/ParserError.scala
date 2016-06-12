package org.raml.parser

import org.yaml.snakeyaml.nodes.Node

case class ParserError(error: String, node: Option[Node] = None)
