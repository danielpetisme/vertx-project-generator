package io.vertx.gradle.generator.config

import java.nio.file.Paths

class GeneratorConstants {

  companion object {
    val GENERATOR_ROOT_DIR = Paths.get("..").toAbsolutePath().normalize().toFile()
    val BASE_BUILD_DIR = Paths.get("./build").toAbsolutePath().normalize().toFile()

    val CONFIG_DIR = GENERATOR_ROOT_DIR.resolve("generator/config")
    val TYPE_DIR = GENERATOR_ROOT_DIR.resolve("generator/project")
    val BUILD_TOOLS_DIR = GENERATOR_ROOT_DIR.resolve("generator/buildTool")

    val DEFAULT_TYPE = "core"
    val DEFAULT_GROUP_ID = "com.acme"
    val DEFAULT_ARTIFACT_ID = "sample"
    val DEFAULT_LANGUAGE = "java"
    val DEFAULT_BUILD_TOOL = "maven"
    val DEFAULT_VERTX_VERSION = "3.5.3"
    val DEFAULT_VERTX_DEPENDENCIES = ""
    val DEFAULT_ARCHIVE_FORMAT = "zip"
  }
}
