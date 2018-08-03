package io.vertx.gradle.generator.provider

import io.vertx.gradle.generator.provider.VertxProjectProvider.Companion.provideProjects
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

class EnvironmentArgumentsProvider : ArgumentsProvider {

  override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
    val type = System.getenv("VERTX_PROJECT_TYPE") ?: ""
    val language = System.getenv("VERTX_PROJECT_LANGUAGE") ?: ""
    val buildTool = System.getenv("VERTX_PROJECT_BUILDTOOL") ?: ""

    return provideProjects(
      types = arrayOf(type),
      buildTools = arrayOf(buildTool),
      languages = arrayOf(language)
    ).stream().map { Arguments.of(it) }
  }

}
