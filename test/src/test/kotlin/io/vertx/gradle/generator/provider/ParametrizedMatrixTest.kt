package io.vertx.gradle.generator.provider

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

/**
 * Used to inject environment variables to parametrized tests.
 * If no env var provided, all the configurations will be tested.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ParameterizedTest
@ArgumentsSource(EnvironmentArgumentsProvider::class)
annotation class ParametrizedMatrixTest
