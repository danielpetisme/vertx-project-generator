package io.vertx.gradle.generator

import io.vertx.gradle.generator.model.VertxProject
import io.vertx.gradle.generator.utils.GradleUtils.Companion.runGradleForProject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VertxProjectAssemblyTest {

  @Test
  fun `should generate a Zip`() {
    val project = VertxProject(archiveFormat = "zip")
    "clean assembleVertxProject".runGradleForProject(project)
    val zipFile = project.buildDir.resolve("${project.artifactId}.zip")
    assertThat(zipFile).exists().isFile()
  }

  @Test
  fun `should generate a Tar`() {
    val project = VertxProject(archiveFormat = "tar")
    "clean assembleVertxProject".runGradleForProject(project)
    val tarFile = project.buildDir.resolve("${project.artifactId}.tar")
    assertThat(tarFile).exists().isFile()
  }

  @Test
  fun `should generate a Tar compressed with GZIP`() {
    val project = VertxProject(archiveFormat = "tar.gz")
    "clean assembleVertxProject".runGradleForProject(project)
    val tarGzFile = project.buildDir.resolve("${project.artifactId}.tar.gz")
    assertThat(tarGzFile).exists().isFile()
  }

  @Test
  fun `should generate a Tar compressed with BZIP2`() {
    val project = VertxProject(archiveFormat = "tar.bz")
    "clean assembleVertxProject".runGradleForProject(project)
    val tarBzFile = project.buildDir.resolve("${project.artifactId}.tar.bz")
    assertThat(tarBzFile).exists().isFile()
  }

}
