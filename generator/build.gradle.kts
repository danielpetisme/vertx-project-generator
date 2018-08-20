//Defaults
ext["default_type"] = "core"
ext["default_groupId"] = "io.vertx"
ext["default_artifactId"] = "starter"
ext["default_buildTool"] = "maven"
ext["default_language"] = "java"
ext["default_vertxVersion"] = "3.5.3"
ext["default_vertxDependencies"] = emptyList<String>()
ext["default_archiveFormat"] = "zip"

ext["type"] = project.findProperty("type") ?: ext["default_type"]
ext["groupId"] = project.findProperty("groupId") ?: ext["default_groupId"]
ext["artifactId"] = project.findProperty("artifactId") ?: ext["default_artifactId"]

val buildTool: String = project.findProperty("buildTool") as String? ?: ext["default_buildTool"] as String
ext["buildTool"] = buildTool.toLowerCase()

val language: String = project.findProperty("language") as String? ?: ext["default_language"] as String
ext["language"] = language.toLowerCase()

ext["vertxVersion"] = project.findProperty("vertxVersion") ?: ext["default_vertxVersion"]

val dependencies: String? = project.findProperty("vertxDependencies") as String?
ext["vertxDependencies"] = if (dependencies.isNullOrBlank()) ext["default_vertxDependencies"] else dependencies?.split(",")?.map { it.trim() }

val archiveFormat: String = project.findProperty("archiveFormat") as String? ?: ext["default_archiveFormat"] as String
ext["archiveFormat"] = archiveFormat.toLowerCase()

ext["projectOutputDir"] = buildDir.resolve(ext["artifactId"] as String)

val clean = task("clean") {
  doLast {
    delete(ext["projectOutputDir"])
  }
}

val zip = task<Zip>("zip") {
  archiveName = "${ext["artifactId"]}.${ext["archiveFormat"]}"
  from(buildDir)
  include("${ext["artifactId"]}/**")
  destinationDir = buildDir
}

val tar = task<Tar>("Tar") {
  archiveName = "${ext["artifactId"]}.${ext["archiveFormat"]}"
  from(buildDir)
  include("${ext["artifactId"]}/**")
  destinationDir = buildDir
  compression = when (ext["archiveFormat"]) {
    "tar.gz" -> Compression.GZIP
    "tar.bz" -> Compression.BZIP2
    else -> Compression.NONE
  }
}

task("assembleVertxProject") {
  if (listOf("tar", "tar.gz", "tar.bz").contains(ext["archiveFormat"])) {
    finalizedBy(tar)
  } else {
    finalizedBy(zip)
  }
}

task("generateVertxProject") {
  dependsOn("assembleVertxProject")
}

apply {
  from("config/config.gradle.kts")
  from("buildTool/buildTool.gradle.kts")
  from("project/project.gradle.kts")
}
