name := """data-accessor-mysql-web"""
organization := "com.ideal.linked"

version := "0.6-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

libraryDependencies += guice
libraryDependencies += "com.ideal.linked" %% "scala-common" % "0.6-SNAPSHOT"
libraryDependencies += "com.ideal.linked" %% "toposoid-common" % "0.6-SNAPSHOT"
libraryDependencies ++= Seq(evolutions)
libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.4.1"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"
libraryDependencies += "io.jvm.uuid" %% "scala-uuid" % "0.3.1"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ideal.linked.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ideal.linked.binders._"
