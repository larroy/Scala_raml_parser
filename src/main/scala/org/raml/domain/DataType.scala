package org.raml.domain


case class DataType(
  name: String,
  // Multiple types represent Union Types, general case is just one
  typ: IndexedSeq[Type],
  comment: String = "",
  description: String = "",
  required: Boolean = false,
  // Multiple types represent multiple inheritance
  parents: IndexedSeq[DataType] = Vector.empty[DataType]
) {
  def isUnion: Boolean = typ.length > 1
  def singleton: Boolean = typ.length == 1
}


