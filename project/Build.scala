import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "websocket_example"
  val appVersion      = "1.1"

  val appDependencies: Seq[ModuleID] = Seq(
    "websocket_plugin" % "websocket_plugin_2.10" % "0.4.0",
    "com.github.johnreedlol" %% "scala-trace-debug" % "3.0.3"
  )


  val main: Project = play.Project(appName, appVersion, appDependencies).settings(

      resolvers += Resolver.url("TPTeam Repository", url("http://tpteam.github.io/snapshots/"))(Resolver.ivyStylePatterns),

      resolvers += "johnreed2 bintray" at "http://dl.bintray.com/content/johnreed2/maven"

  )

}
