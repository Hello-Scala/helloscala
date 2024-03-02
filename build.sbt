lazy val helloScalaBackend = (project in file("hello-scala-backend"))
    .enablePlugins(PlayScala)
    .settings(
        name := """hello-scala-backend""",
        organization := "com.helloscala",
        version := "0.0.1-SNAPSHOT",
        crossScalaVersions := Seq("2.13.12"),
        scalaVersion := crossScalaVersions.value.head,
        libraryDependencies ++= Seq(
            guice,
            "org.playframework" %% "play" % "3.0.1"
        ),
        scalacOptions ++= Seq(
            "-feature",
            "-Werror"
        )
    )
