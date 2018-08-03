package io.vertx.gradle.generator.provider

import io.vertx.gradle.generator.provider.VertxProjectProvider.Companion.provideProjects
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.support.AnnotationConsumer
import java.util.stream.Stream

class VertxProjectArgumentsProvider : ArgumentsProvider, AnnotationConsumer<TestWithVertxProjects> {

  var annotation: TestWithVertxProjects? = null

  override fun accept(annotation: TestWithVertxProjects?) {
    this.annotation = annotation
  }

  override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
    provideProjects(
      types = this.annotation!!.types,
      buildTools = this.annotation!!.buildTools,
      languages = this.annotation!!.languages
    ).stream().map { Arguments.of(it) }

}
