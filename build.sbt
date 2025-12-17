import de.heikoseeberger.sbtheader.License

name := """data-accessor-mysql-web"""
organization := "com.ideal.linked"

version := "0.7-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(AutomateHeaderPlugin)

scalaVersion := "3.3.6"
resolvers += Resolver.mavenLocal
libraryDependencies += guice
libraryDependencies += "com.ideal.linked" %% "scala-common" % "0.7-SNAPSHOT" exclude("org.slf4j","slf4j-api")
libraryDependencies += "com.ideal.linked" %% "toposoid-common" % "0.7-SNAPSHOT" exclude("org.slf4j","slf4j-api")
libraryDependencies ++= Seq(evolutions)
//libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.0.0" exclude("org.slf4j","slf4j-api")
//libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0" exclude("org.slf4j","slf4j-api")
//libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.4.1" exclude("org.slf4j","slf4j-api")
libraryDependencies += "org.playframework" %% "play-slick" % "6.1.0" exclude("org.slf4j","slf4j-api")
libraryDependencies += "org.playframework" %% "play-slick-evolutions" % "6.1.0" exclude("org.slf4j","slf4j-api")
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.5.2" exclude("org.slf4j","slf4j-api")
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33" exclude("org.slf4j","slf4j-api")
//libraryDependencies += "io.jvm.uuid" %% "scala-uuid" % "0.3.1"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test exclude("org.slf4j","slf4j-api")
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.36" 

organizationName := "Linked Ideal LLC.[https://linked-ideal.com/]"
startYear := Some(2021)
licenses += ("AGPL-3.0-or-later", new URL("http://www.gnu.org/licenses/agpl-3.0.en.html"))
headerLicense := Some(License.AGPLv3("2025", organizationName.value))

