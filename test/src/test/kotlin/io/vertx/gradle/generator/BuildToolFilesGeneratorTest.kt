package io.vertx.gradle.generator

import io.vertx.gradle.generator.model.VertxProject
import io.vertx.gradle.generator.utils.GradleUtils.Companion.runGradleForProject
import org.assertj.core.api.Assertions
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import java.io.File
import java.util.function.Predicate

class BuildToolFilesGeneratorTest {

  private val fileCanExecuteCondition = Condition<File>(Predicate { file: File -> file.canExecute() }, "Is Executable")

  @Test
  fun `should generate maven wrapper files`() {
    val project = VertxProject(buildTool = "maven")
    "clean buildToolFiles".runGradleForProject(project)
    project.apply {
      Assertions.assertThat(file(".mvn")).exists().isDirectory()
      Assertions.assertThat(file("mvnw.cmd")).exists().isFile()
      Assertions.assertThat(file("mvnw")).exists().isFile().has(fileCanExecuteCondition)
      val pomXml = file("pom.xml")
      Assertions.assertThat(pomXml).exists().isFile()
      val pomXmlContent = pomXml.readText()
      Assertions.assertThat(pomXmlContent).containsSubsequence(
        "<groupId>${project.groupId}</groupId>",
        "<artifactId>${project.artifactId}</artifactId>",
        "<vertx.version>${project.vertxVersion}</vertx.version>",
        "<artifactId>vertx-core</artifactId>"
      )
    }
  }

  @Test
  fun `should generate pomXml with dependencies`() {
    val project = VertxProject(buildTool = "maven", vertxDependencies = "vertx-web,vertx-config")
    "clean buildToolFiles".runGradleForProject(project)
    project.apply {
      val pomXml = file("pom.xml")
      Assertions.assertThat(pomXml).exists().isFile()
      val pomXmlContent = pomXml.readText()
      Assertions.assertThat(pomXmlContent).containsSubsequence(
        "<artifactId>vertx-web</artifactId>",
        "<artifactId>vertx-config</artifactId>"
      )
    }
  }

  @Test
  fun `should generate gradle wrapper files`() {
    val project = VertxProject(buildTool = "gradle")
    "clean buildToolFiles".runGradleForProject(project)
    project.apply {
      Assertions.assertThat(file("gradle")).exists().isDirectory()
      Assertions.assertThat(file("gradlew.bat")).exists().isFile()
      Assertions.assertThat(file("gradlew")).exists().isFile().has(fileCanExecuteCondition)
      val buildGradle = file("build.gradle")
      Assertions.assertThat(buildGradle).exists().isFile()
      val buildGradleContent = buildGradle.readText()
      Assertions.assertThat(buildGradleContent).containsSubsequence(
        "vertxVersion = '${vertxVersion}",
        "group = '${project.groupId}'",
        "implementation \"io.vertx:vertx-core:\$vertxVersion\""
      )
      val settingsGradle = file("settings.gradle")
      Assertions.assertThat(settingsGradle).exists().isFile()
      val settingsGradleContent = settingsGradle.readText()
      Assertions.assertThat(settingsGradleContent).containsSubsequence(
        "rootProject.name = '$artifactId'"
      )
    }
  }

  @Test
  fun `should generate buildGradle with dependencies`() {
    val project = VertxProject(buildTool = "gradle", vertxDependencies = "vertx-web,vertx-config")
    "clean buildToolFiles".runGradleForProject(project)
    project.apply {
      val buildGradle = file("build.gradle")
      val buildGradleContent = buildGradle.readText()
      Assertions.assertThat(buildGradleContent).containsSubsequence(
        "implementation \"io.vertx:vertx-web:\$vertxVersion\"",
        "implementation \"io.vertx:vertx-config:\$vertxVersion\""
      )
    }
  }
}
