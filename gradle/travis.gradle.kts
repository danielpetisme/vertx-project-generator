val SKIP_GRADLE_FILE = { _: Any, name: String -> !name.endsWith(".gradle.kts") }

val types = projectDir.resolve("generator/project").list(SKIP_GRADLE_FILE)
val buildTools = projectDir.resolve("generator/buildTool").list(SKIP_GRADLE_FILE)
val languages = listOf("java", "kotlin")

var template = """# DO NOT EDIT MANUALLY, RUN ./gradlew travis TO GENERATE.
sudo: false

language: java

before_cache:
- rm -f  ${'$'}HOME/.m2
- rm -f  ${'$'}HOME/.gradle/caches/modules-2/modules-2.lock
- rm -fr ${'$'}HOME/.gradle/caches/*/plugin-resolution/

cache:
  directories:
  - ${'$'}HOME/.m2
  - ${'$'}HOME/.gradle/caches/
  - ${'$'}HOME/.gradle/wrapper/

jdk:
  - oraclejdk8


jobs:
  include:
  - stage: "Generation Tests"
    name: "Basic generation"
    script: ./gradlew :test:cleanTest :test:test -PexcludeTags=test-execution
"""

for (type in types) {
  for (buildTool in buildTools) {
    for (language in languages) {
      template += """
  - stage: "Execution Tests"
    name: "$type-$language-$buildTool"
    env: VERTX_PROJECT_TYPE=$type VERTX_PROJECT_BUILDTOOL=$buildTool VERTX_PROJECT_LANGUAGE=$language
    script: ./gradlew :test:cleanTest :test:test -PincludeTags=test-execution"""
    }
  }
}

val travis = task("travis") {
  doLast {
    projectDir.resolve(".travis.yml").writeText(template)
  }
}
