
name := "palabrot"

version := "7.0.1"

scalaVersion := "2.13.4"

/** API Functional Wraper */
libraryDependencies += "org.augustjune" %% "canoe" % "0.5.1"

/** Logging */
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

/** Testing */
libraryDependencies += "org.scalameta" %% "munit" % "0.7.22" % Test
libraryDependencies += "org.typelevel" %% "munit-cats-effect-2" % "0.13.1" % Test
testFrameworks += new TestFramework("munit.Framework")


/** Scalafix configuration */
val ENABLE_SemanticDB = true
val ENABLE_RemoveUnused = true // required by `RemoveUnused` rule

inThisBuild(
  List(
    addCompilerPlugin(scalafixSemanticdb),
    semanticdbEnabled := ENABLE_SemanticDB,
    semanticdbVersion := scalafixSemanticdb.revision  //use Scalafix compatible version
  )
)
scalacOptions ++= {
  if (ENABLE_RemoveUnused) List("-Yrangepos", "-Ywarn-unused")
  else Nil
}

/** Elastic Search */
 val elastic4sVersion = "7.10.3"
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test"
)