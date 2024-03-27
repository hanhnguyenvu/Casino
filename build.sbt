ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "HanhNguyenVuCassino"
  )
libraryDependencies += "org.scalafx" % "scalafx_3" % "20.0.0-R31"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % "test"