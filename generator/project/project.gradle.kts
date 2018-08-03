import io.vertx.gradle.generator.FreemarkerTemplateEngine

val projectDir = "${project.projectDir}/project"

val ext: ExtraPropertiesExtension by extensions
val type: String by ext
val groupId: String by ext
val artifactId: String by ext
val language: String by ext
val projectOutputDir: File by ext

val defaultPackageName = "${ext["default_groupId"]}.${ext["default_artifactId"]}"
val packageName = "$groupId.$artifactId"

fun packageDir(packageName: String) = packageName.replace(".", File.separator)

fun sourcesCopyTask(name: String, sourceSet: String) = task<Copy>(name) {
  from(fileTree("$projectDir/$type/src/$sourceSet/$language"))
  exclude("**/*.ftl")
  eachFile { this.path = this.path.replace(packageDir(defaultPackageName), packageDir(packageName)) }
  filter { line -> line.replace(defaultPackageName, packageName) }
  includeEmptyDirs = false
  into("$projectOutputDir/src/$sourceSet/$language")
}

fun resourcesCopyTask(name: String, sourceSet: String) = task<Copy>(name) {
  from(fileTree("$projectDir/$type/src/$sourceSet/resources"))
  exclude("**/*.ftl")
  includeEmptyDirs = true
  into("$projectOutputDir/src/$sourceSet/resources")
}

val mainSources = sourcesCopyTask("mainSources", "main")

val mainResources = resourcesCopyTask("mainResources", "main")

val testSources = sourcesCopyTask("testSources", "test")

val testResources = resourcesCopyTask("testResources", "test")

val projectFiles = task("projectFiles") {
  dependsOn(mainSources, mainResources, testSources, testResources)
  doLast {
    FreemarkerTemplateEngine(from = file("$projectDir/$type"), into = projectOutputDir).render(ext.properties)
  }
}

tasks["assembleVertxProject"].dependsOn(projectFiles)
