package org.raml.parser

import cats.data.NonEmptyList
import org.raml.domain.Api
import org.specs2.mutable._

class ProtocolsSpec extends Specification {
  def parseValid(spec: String): Api = {
    val ramlParser = new RamlParser()
    val result = ramlParser(spec)
    result.isValid should beTrue
    result.toOption.get
  }

  "Protocols" >> {
    val spec =
      """
        |#%RAML 1.0
        |title: Salesforce Chatter REST API
        |version: v28.0
        |protocols: [ HTTP, HTTPS ]
        |baseUri: https://na1.salesforce.com/services/data/{version}/chatter
      """.stripMargin
    val api = parseValid(spec)
    api.protocols must have size(2)
    api.protocols.toSet must contain(allOf("http", "https"))
  }

  "Implicit Protocols" >> {
    val spec =
      """
        |#%RAML 1.0
        |title: Salesforce Chatter REST API
        |version: v28.0
        |baseUri: https://na1.salesforce.com/services/data/{version}/chatter
      """.stripMargin
    val api = parseValid(spec)
    api.protocols must have size(1)
    api.protocols.toSet must contain(allOf("https"))
  }

  "Invalid Protocols" >> {
    val spec =
      """
        |#%RAML 1.0
        |title: Salesforce Chatter REST API
        |version: v28.0
        |protocols: [FTP]
        |baseUri: https://na1.salesforce.com/services/data/{version}/chatter
      """.stripMargin
    val ramlParser = new RamlParser()
    val result = ramlParser(spec)
    result.isValid should beFalse
    // TODO: check the error exist and complains about the protocol
    val x: NonEmptyList[ParserError] = result.fold(x⇒x, _⇒ sys.error("not possible"))
    x.head.error must beEqualTo("Invalid protocol FTP")
  }
}