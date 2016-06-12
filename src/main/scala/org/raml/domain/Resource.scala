package org.raml.domain

import scala.collection.immutable.IndexedSeq

/**
  * A REST resource
  */
case class Resource(
  displayName: String = "",
  description: String = "",
  relativeUri: String = "",
  nestedResources: IndexedSeq[Resource] = IndexedSeq.empty[Resource],
  methods: IndexedSeq[Method] = IndexedSeq.empty[Method]
)
