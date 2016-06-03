package org.raml.domain

import scala.collection.immutable.IndexedSeq

sealed trait Type {
}

final case class ObjectType(
  properties: IndexedSeq[DataType] = Vector.empty[DataType],
  additionalProperties: Boolean = true,
  minProperties: Int = 0,
  maxProperties: Int = Int.MaxValue,
  discriminator: Option[String] = None,
  discriminatorValue: Option[String] = None
) extends Type

final case class ArrayType(
  items: Option[Type] = None,
  minItems: Int = 0,
  maxItems: Int = Int.MaxValue,
  uniqueItems: Boolean = false
) extends Type

final case class StringType(
  pattern: String = "",
  minLength: Int = 0,
  maxLength: Int = Int.MaxValue
) extends Type

final case class NumberType(
  min: Option[Int] = None,
  max: Option[Int] = None,
  format: Option[NumberFormat] = None,
  multipleOf: Option[Int] = None
) extends Type

case class BoolType(
) extends Type

case class DateOnlyType(
) extends Type

case class TimeOnlyType(
) extends Type

case class DateTimeOnlyType(
) extends Type

case class DateTimeType(
  format: DateTimeFormat
) extends Type

case class FileType(
  fileTypes: IndexedSeq[String] = Vector.empty[String],
  minLength: Int = 0,
  maxLength: Int = Int.MaxValue
) extends Type

// Do we need this?
case class NullType(
) extends Type


sealed trait NumberFormat

case class Int32Format() extends NumberFormat

case class Int64Format() extends NumberFormat

case class IntFormat() extends NumberFormat

case class LongFormat() extends NumberFormat

case class FloatFormat() extends NumberFormat

case class DoubleFormat() extends NumberFormat


sealed trait DateTimeFormat

case class Rfc3339() extends DateTimeFormat

case class Rfc2616() extends DateTimeFormat