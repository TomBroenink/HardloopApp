name := """hardloopapp-backend"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "mysql" % "mysql-connector-java" % "5.1.35",
  "org.jasypt" % "jasypt" % "1.9.2"
)

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true