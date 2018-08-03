package io.vertx.gradle.generator.provider

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ParameterizedTest
@ArgumentsSource(VertxProjectArgumentsProvider::class)
annotation class TestWithVertxProjects(
  val types: Array<String> = [],
  val buildTools: Array<String> = [],
  val languages: Array<String> = [])
