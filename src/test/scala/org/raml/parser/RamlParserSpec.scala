package org.raml.parser

import org.specs2.mutable._

class RamlParserSpec extends Specification {
  "" should {
    "" in {
      val spec =
        """
          |#%RAML 1.0
          |title: Salesforce Chatter REST API
          |version: v28.0
          |protocols: [ HTTP, HTTPS ]
          |baseUri: https://na1.salesforce.com/services/data/{version}/chatter
        """.stripMargin
      val ramlParser = new RamlParser()
      val result = ramlParser(spec)
      ok
    }
  }
}