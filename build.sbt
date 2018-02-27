import sbt.Keys._

val scalaV = "2.12.4"
val simulacrumV = "0.12.0"
val catsV = "1.0.1"
val textRazorV = "1.0.11"

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
  "org.typelevel" %% "cats-core" % catsV,
  "com.textrazor" % "textrazor" % textRazorV
)

scalacOptions in (Compile, console) ++= Seq(
  "-i", "myrepl.init"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

enablePlugins(TutPlugin)

tutTargetDirectory := file(".")
