package org.raml.domain

import scala.collection.immutable.IndexedSeq

case class Api(
  description: String,
  version: String,
  baseUri: String,
  baseUriParameters: List[Parameter],
  protocols: IndexedSeq[String],
  mediaType: IndexedSeq[String],
  documentation: IndexedSeq[DocItem],
  // TODO: traits

  /**
    * Data types of the API
    */
  types: IndexedSeq[DataType],

  /**
    * Resources
    */
  resources: IndexedSeq[Resource],

  ramlVersion: String
)
