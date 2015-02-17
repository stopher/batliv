name := """baater"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  ws,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.4",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "flot" % "0.8.0",  
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
