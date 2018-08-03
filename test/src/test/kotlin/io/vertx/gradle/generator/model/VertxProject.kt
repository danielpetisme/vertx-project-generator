package io.vertx.gradle.generator.model

import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_ARCHIVE_FORMAT
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_ARTIFACT_ID
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_BUILD_TOOL
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_TYPE
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_GROUP_ID
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_LANGUAGE
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_VERTX_DEPENDENCIES
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.DEFAULT_VERTX_VERSION
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.BASE_BUILD_DIR
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.GENERATOR_ROOT_DIR
import java.io.File
import java.util.*

data class VertxProject(
  val type: String = DEFAULT_TYPE,
  val groupId: String = DEFAULT_GROUP_ID,
  val artifactId: String = DEFAULT_ARTIFACT_ID,
  val buildTool: String = DEFAULT_BUILD_TOOL,
  val language: String = DEFAULT_LANGUAGE,
  val vertxVersion: String = DEFAULT_VERTX_VERSION,
  val vertxDependencies: String = DEFAULT_VERTX_DEPENDENCIES,
  val archiveFormat: String = DEFAULT_ARCHIVE_FORMAT,
  val gradleProjectDir: File = GENERATOR_ROOT_DIR,
  val buildDir: File = BASE_BUILD_DIR.resolve(UUID.randomUUID().toString()),
  val projectOutputDir: File = File("$buildDir/$artifactId")
) {

  fun file(filename: String): File = projectOutputDir.resolve(filename)

}
