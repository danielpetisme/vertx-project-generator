package io.vertx.gradle.generator

import io.vertx.gradle.generator.model.VertxProject
import io.vertx.gradle.generator.utils.GradleUtils.Companion.runGradleForProject
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class ProjectFilesGeneratorTest {

  private fun VertxProject.verifySources() {
    assertThat(file("src/main/$language")).exists().isDirectory()
    assertThat(file("src/test/$language")).exists().isDirectory()
  }

  private fun VertxProject.verifyPackage() {
    val packageName = "$groupId.$artifactId"
    val packageDir = packageName.replace(".", File.separator)
    val sources = file("src").walkBottomUp()
      .filter { file -> file.isFile && !file.path.contains("resources") }

    sources.forEach { file ->
      Assertions.assertThat(file.path).contains(packageDir)
      Assertions.assertThat(file.readText()).contains("package $packageName")
    }
  }

  @Test
  fun `should generate project files`() {
    val project = VertxProject()
    "clean projectFiles".runGradleForProject(project)
    project.verifySources()
    project.verifyPackage()
  }

  @Test
  fun `should generate project files even if dash in groupId or artifactId`() {
    val projectWithDashedGroupId = VertxProject(groupId = "com.acme-awesome")
    "clean projectFiles".runGradleForProject(projectWithDashedGroupId)
    projectWithDashedGroupId.verifyPackage()
    val projectWithDashedArtifactId = VertxProject(artifactId = "some-app")
    "clean projectFiles".runGradleForProject(projectWithDashedArtifactId)
    projectWithDashedArtifactId.verifyPackage()
  }
  
}
