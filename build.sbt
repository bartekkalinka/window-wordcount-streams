name := "window-wordcount-streams"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.11"
  Seq(
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.twitter" % "hbc-core" % "2.2.0",
    "com.twitter" % "hbc-twitter4j" % "2.2.0",
    "org.slf4j" % "slf4j-simple" % "1.7.21"
  )
}

mainClass := Some("Main")
    
