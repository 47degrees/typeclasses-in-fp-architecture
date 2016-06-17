import sbt.Keys._

val scalaV = "2.11.7"
val simulacrumV = "0.7.0"
val circeV = "0.4.1"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

scalacOptions ++= Seq(
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps")

scalaVersion := scalaV

resolvers ++= Seq(
    Resolver.mavenLocal,
    DefaultMavenRepository,
    "jcenter" at "http://jcenter.bintray.com",
    "47 Degrees Bintray Repo" at "http://dl.bintray.com/47deg/maven",
    Resolver.typesafeRepo("releases"),
    Resolver.typesafeRepo("snapshots"),
    Resolver.typesafeIvyRepo("snapshots"),
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.defaultLocal
  ) 

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaV,
  "com.github.mpilquist" %% "simulacrum" % simulacrumV,
  "io.circe" %% "circe-core" % circeV,
  "io.circe" %% "circe-generic" % circeV,
  "io.circe" %% "circe-parser" % circeV
)

scalacOptions in (Compile, console) ++= Seq(
  "-i", "myrepl.init"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

tutSettings

tutTargetDirectory := file(".")
