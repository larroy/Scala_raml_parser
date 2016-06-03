package org.raml.domain

import scala.collection.immutable.IndexedSeq

class Method(
  displayName: String,
  description: String,
  queryParameters: IndexedSeq[Parameter],
  queryString: String,
  headers: IndexedSeq[Header],
  responses: IndexedSeq[Response]
)
