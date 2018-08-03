package io.vertx.gradle.generator.utils

import io.vertx.gradle.generator.model.VertxProject
import org.assertj.core.api.Assertions
import org.gradle.testkit.runner.GradleRunner

class GradleUtils {

  companion object {

    fun String.runGradleForProject(project: VertxProject) {
      val tasks = this.split("\\s".toRegex())
      val arguments = listOf(
        "-Dorg.gradle.daemon=false",
        "-Dorg.gradle.project.buildDir=${project.buildDir}",
        *tasks.toTypedArray(),
        "-Ptype=${project.type}",
        "-PgroupId=${project.groupId}",
        "-PartifactId=${project.artifactId}",
        "-Planguage=${project.language}",
        "-PbuildTool=${project.buildTool}",
        "-PvertxVersion=${project.vertxVersion}",
        "-PvertxDependencies=${project.vertxDependencies}",
        "-ParchiveFormat=${project.archiveFormat}"
      )
      val runner = GradleRunner.create()
        .withProjectDir(project.gradleProjectDir)
        .withArguments(arguments)
        .withDebug(true)
        .build()

      Assertions.assertThat(runner.output).contains("BUILD SUCCESSFUL")

      Assertions.assertThat(project.projectOutputDir).exists()
      Assertions.assertThat(project.projectOutputDir.list()).isNotEmpty
    }

  }

}
