package io.vertx.gradle.generator

import io.vertx.gradle.generator.config.GeneratorConstants.Companion.CONFIG_DIR
import io.vertx.gradle.generator.model.VertxProject
import io.vertx.gradle.generator.utils.GradleUtils.Companion.runGradleForProject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class ConfigFilesGeneratorTest {

  @Test
  fun `should generate config files`() {
    val project = VertxProject()
    "clean configFiles".runGradleForProject(project)
    File("$CONFIG_DIR/files").list()
      .forEach { filename -> Assertions.assertThat(project.file(".${filename.removePrefix("_")}")).exists().isFile() }
  }

}
