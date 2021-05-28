name := "warehouseapi"

version := "0.1"

scalaVersion := "2.13.6"


val akkaHttpVersion = "10.2.4"
val akkaVersion = "2.6.8"
val scalaTestVersion = "3.2.9"
val slf4jVersion = "1.7.25"
val logbackVersion = "1.2.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,

  "net.debasishg" %% "redisclient" % "3.30",

  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,

  "org.slf4j" % "jul-to-slf4j" % slf4jVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion
)