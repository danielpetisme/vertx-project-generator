package io.vertx.gradle.generator

import io.vertx.gradle.generator.model.BuildTool
import io.vertx.gradle.generator.model.BuildTool.GRADLEW
import io.vertx.gradle.generator.model.BuildTool.MVNW
import io.vertx.gradle.generator.model.VertxProject
import io.vertx.gradle.generator.provider.ParametrizedMatrixTest
import io.vertx.gradle.generator.utils.GradleUtils.Companion.runGradleForProject
import io.vertx.gradle.generator.utils.SystemProcessUtils.Companion.execute
import io.vertx.gradle.generator.utils.SystemProcessUtils.Companion.executeAndFindFirstInOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag

@Tag("test-execution")
class VertxProjectGeneratorMatrixTest {

  private fun VertxProject.buildTool(): BuildTool = if (buildTool == "maven") MVNW else GRADLEW

  @ParametrizedMatrixTest
  fun `should generate a testable project`(project: VertxProject) {
    "clean generateVertxProject".runGradleForProject(project)
    val testCommand = project.buildTool().testCommand()
    val testSuccess = testCommand.execute(workingDir = project.projectOutputDir)
    assertThat(testSuccess).isEqualTo(0)
  }

  @ParametrizedMatrixTest
  fun `should generate a runnable project`(project: VertxProject) {
    "clean generateVertxProject".runGradleForProject(project)
    val runCommand = project.buildTool().runCommand()
    val expected = when (project.buildTool) {
      "maven" -> "Succeeded in deploying verticle"
      "gradle" -> "Starting the vert.x application in redeploy mode"
      else -> ""
    }
    val runSuccess = runCommand
      .executeAndFindFirstInOutput(workingDir = project.projectOutputDir) { line -> line!!.contains(expected, true) }
    assertThat(runSuccess).isTrue()
  }

  @ParametrizedMatrixTest
  fun `should generate an executable fat jar`(project: VertxProject) {
    "clean generateVertxProject".runGradleForProject(project)
    val packageCommand = project.buildTool().packageCommand()
    val packageSuccess = packageCommand
      .executeAndFindFirstInOutput(workingDir = project.projectOutputDir) { line -> line!!.contains("SUCCESS", true) }
    assertThat(packageSuccess).isTrue()
    val jar = "${project.buildTool().jarDir()}/${project.artifactId}-1.0.0-SNAPSHOT-fat.jar"
    val jarFile = project.projectOutputDir.resolve(jar)
    assertThat(jarFile).exists()
    val fatJarExecSuccess = "java -jar $jar"
      .executeAndFindFirstInOutput(workingDir = project.projectOutputDir) { line -> line!!.contains("Succeeded in deploying verticle", true) }
    assertThat(fatJarExecSuccess).isTrue()
  }
}
