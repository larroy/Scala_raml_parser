package org.raml.domain

import scala.collection.immutable.IndexedSeq

case class Api(
  title: String,
  description: String = "",
  version: String = "",
  baseUri: String = "",
  baseUriParameters: List[Parameter] = List.empty[Parameter],
  protocols: IndexedSeq[String] = IndexedSeq.empty[String],
  mediaType: IndexedSeq[String] = IndexedSeq.empty[String],
  documentation: IndexedSeq[DocItem] = IndexedSeq.empty[DocItem],
  // TODO: traits

  /**
    * Data types of the API
    */
  types: IndexedSeq[DataType] = IndexedSeq.empty[DataType],

  /**
    * Resources
    */
  resources: IndexedSeq[Resource] = IndexedSeq.empty[Resource],

  ramlVersion: String = "1.0"
)
