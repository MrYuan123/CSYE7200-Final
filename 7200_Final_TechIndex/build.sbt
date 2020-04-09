name := "7200_Final_TechIndex"

version := "1.0"

//scalaVersion := "2.11.12"

scalaVersion := "2.11.12"
val sparkVersion = "2.4.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.commons" % "commons-math3" % "3.0"
)
