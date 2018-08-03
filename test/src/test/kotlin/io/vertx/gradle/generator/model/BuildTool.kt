package io.vertx.gradle.generator.model


enum class BuildTool {

  MVNW {
    private val command: String = if (isWindows()) "./mvnw.cmd" else "./mvnw"
    override fun testCommand(): String = "$command test"
    override fun runCommand(): String = "$command -DskipTests=true compile exec:java"
    override fun packageCommand(): String = "$command -DskipTests=true package"
    override fun jarDir(): String = "target"
  },
  GRADLEW {
    private val command: String = if (isWindows()) "./gradlew.bat" else "./gradlew"
    override fun testCommand(): String = "$command test"
    override fun runCommand(): String = "$command run"
    override fun packageCommand(): String = "$command assemble"
    override fun jarDir(): String = "build/libs"
  };

  abstract fun testCommand():String
  abstract fun runCommand(): String
  abstract fun packageCommand(): String
  abstract fun jarDir(): String

  internal fun isWindows(): Boolean = System.getProperty("os.name").contains("WIN", ignoreCase = true)


}
