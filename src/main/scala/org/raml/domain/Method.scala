package org.raml.domain

import scala.collection.immutable.IndexedSeq

case class Method(
  displayName: String = "",
  description: String = "",
  queryParameters: IndexedSeq[Parameter] = IndexedSeq.empty[Parameter],
  queryString: String = "",
  headers: IndexedSeq[Header] = IndexedSeq.empty[Header],
  responses: IndexedSeq[Response] = IndexedSeq.empty[Response]
)
