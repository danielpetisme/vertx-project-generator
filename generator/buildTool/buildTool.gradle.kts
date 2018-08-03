import io.vertx.gradle.generator.FreemarkerTemplateEngine

val buildToolDir = "${project.projectDir}/buildTool"

val ext:ExtraPropertiesExtension by extensions

val buildTool: String by ext
val projectOutputDir: File by ext

val maven = task<Copy>("maven") {
  val mavenDir = file("$buildToolDir/maven")
  from(mavenDir) {
    include("mvnw", "mvnw.cmd")
  }
  from("$mavenDir/_mvn") {
    into(".mvn")
  }
  into(projectOutputDir)

  doLast {
    FreemarkerTemplateEngine(from = mavenDir, into = projectOutputDir).render(ext.properties)
  }
}

val gradle = task<Copy>("gradle") {
  val gradleDir = file("$buildToolDir/gradle")
  from(gradleDir) {
    include("gradlew", "gradlew.bat")
  }
  from("$gradleDir/gradle") {
    into("gradle")
  }
  into(projectOutputDir)

  doLast {
    FreemarkerTemplateEngine(from = gradleDir, into = projectOutputDir).render(ext.properties)
  }
}

val buildToolFiles = task("buildToolFiles") {
  dependsOn(maven, gradle)
  maven.onlyIf { buildTool == "maven" }
  gradle.onlyIf { buildTool == "gradle" }
}

tasks["assembleVertxProject"].dependsOn(buildToolFiles)
