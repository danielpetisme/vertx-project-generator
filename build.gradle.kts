defaultTasks("generateVertxProject")

allprojects {

  group = "io.vertx.gradle.generator"
  version = "0.0.1-SNAPSHOT"

  repositories {
    mavenCentral()
    jcenter()
  }
}

apply {
  from("gradle/travis.gradle.kts")
}
