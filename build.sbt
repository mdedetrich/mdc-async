name := """mdc-async"""

version := "0.1"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.8")

libraryDependencies ++= Seq(
  "ch.qos.logback" %  "logback-classic" % "1.1.7" % Provided,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0" % Provided,
  "com.typesafe.akka" %% "akka-actor" % "2.4.4" % Provided
)

organization := "org.mdedetrich"

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := <url>https://github.com/mdedetrich/mdc-async</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:mdedetrich/mdc-async.git</url>
    <connection>scm:git:git@github.com:mdedetrich/mdc-async.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jroper</id>
      <name>James Roper</name>
      <email>james@jazzy.id.au</email>
    </developer>
    <developer>
      <id>yanns</id>
      <name>Yann Simon</name>
    </developer>
    <developer>
      <id>mdedetrich</id>
      <name>Matthew de Detrich</name>
      <email>mdedetrich@gmail.com</email>
    </developer>
  </developers>