name := "window-wordcount-streams"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.11"
  Seq(
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV
  )
}

mainClass := Some("Main")
    
