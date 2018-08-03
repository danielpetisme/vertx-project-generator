package io.vertx.gradle.generator.provider

import io.vertx.gradle.generator.config.GeneratorConstants.Companion.BUILD_TOOLS_DIR
import io.vertx.gradle.generator.config.GeneratorConstants.Companion.TYPE_DIR
import io.vertx.gradle.generator.model.VertxProject

class VertxProjectProvider {

  companion object {
    private val SKIP_GRADLE_FILE = { _: Any, name: String -> !name.endsWith(".gradle.kts") }

    private val allTypes = TYPE_DIR.list(SKIP_GRADLE_FILE)
    private val allLanguages = listOf("java", "kotlin")
    private val allBuildTools = BUILD_TOOLS_DIR.list(SKIP_GRADLE_FILE)

    private fun buildFilter(arr: Array<String>): (String) -> Boolean {
      val filtered = arr.filter { !it.isNullOrBlank() }
      return when (filtered.isEmpty()) {
        true -> { _ -> true }
        false -> { elt -> arr.find { filter -> filter.equals(elt, true) } != null }
      }
    }

    @JvmStatic
    fun provideProjects(types: Array<String>,
                        languages: Array<String>,
                        buildTools: Array<String>): List<VertxProject> {
      var projects: List<VertxProject> = emptyList()
      val filteredTypes = allTypes.filter(buildFilter(types))
      val filteredLanguages = allLanguages.filter(buildFilter(languages))
      val filteredBuildTools = allBuildTools.filter(buildFilter(buildTools))
      for (type in filteredTypes) {
        for (language in filteredLanguages) {
          for (buildTool in filteredBuildTools) {
            projects += VertxProject(type = type, buildTool = buildTool, language = language)
          }
        }
      }
      return projects
    }
  }
}
