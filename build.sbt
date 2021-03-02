name := "pattern-bot"

version := "0.1"

scalaVersion := "2.13.4"

/** API Functional Wraper */
libraryDependencies += "org.augustjune" %% "canoe" % "0.5.1"

/** Logging */
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"