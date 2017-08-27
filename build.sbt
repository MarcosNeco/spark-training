name := "spark-training"

version := "1.0"

scalaVersion := "2.12.3"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "2.1.1",
  "org.apache.spark" % "spark-sql_2.10" % "2.1.1",
  "xmlenc" % "xmlenc" % "0.52"
)