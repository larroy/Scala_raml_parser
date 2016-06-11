lazy val commonSettings = Seq(
  version := "0.1",
  organization := "org.raml",
  name := "parser",
  scalaVersion := "2.11.8",
  scalacOptions := Seq(
    "-target:jvm-1.8",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding", "utf8",
    "-Xlint"

  ),
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("scalaz", "releases"),
    Resolver.bintrayRepo("megamsys", "scala"),
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
  ),

  // Sonatype publishing
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  autoScalaLibrary := false,
  autoScalaLibrary in test := false,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra := (
    <url>https://github.com/openquant</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>http://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>https://github.com/larroy/Scala_raml_parser</url>
      <connection>scm:git@github.com:larroy/Scala_raml_parser.git</connection>
    </scm>
    <developers>
      <developer>
        <id>larroy</id>
        <name>Pedro Larroy</name>
        <url>https://github.com/larroy</url>
      </developer>
    </developers>
  )
)

lazy val commonDependencies = Seq(
  "org.slf4j" % "jcl-over-slf4j" % "1.7.7",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.iheart" %% "ficus" % "1.2.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.yaml" % "snakeyaml" % "1.19-SNAPSHOT"
)

lazy val testDependencies = Seq(
  "org.specs2" %% "specs2" % "3.+" % "test"
)

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(libraryDependencies ++= testDependencies)

