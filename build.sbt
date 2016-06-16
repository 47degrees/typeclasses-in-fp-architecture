import sbt.Keys._

val scalaV = "2.11.7"
val catsV = "0.6.0"
val scalazV = "7.2.0"
val raptureV = "2.0.+"
val scalacheckV = "1.13.0"
val simulacrumV = "0.7.0"

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
  "org.typelevel" %% "cats" % catsV,
  "org.scala-lang" % "scala-compiler" % scalaV,
  "org.scalacheck" %% "scalacheck" % scalacheckV,
  "com.github.mpilquist" %% "simulacrum" % simulacrumV  
)

scalacOptions in (Compile, console) ++= Seq(
  "-i", "myrepl.init"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

tutSettings

tutTargetDirectory := file(".")
