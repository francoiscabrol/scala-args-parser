name := "scala-args-parser"

organization := "io.github.francoiscabrol"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.11.8", "2.12.2")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test" withSources() withJavadoc()
)

scalacOptions := Seq("-feature", "-deprecation")


initialCommands := "import argsparser._"

/**
*  PUBLISH TO SONATYPE
**/
pomIncludeRepository := { _ => false }

licenses := Seq("MIT" -> url("http://www.opensource.org/licenses/mit-license.php"))

homepage := Some(url("https://github.com/francoiscabrol/scala-args-parser"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/francoiscabrol/scala-args-parser"),
    "scm:git@github.com:francoiscabrol/scala-args-parser.git"
  )
)

developers := List(
  Developer(
    id    = "francoiscabrol",
    name  = "Francois Cabrol",
    email = "francois.cabrol",
    url   = url("https://github.com/francoiscabrol")
  )
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
