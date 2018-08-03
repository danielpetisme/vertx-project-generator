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
ext["buildTool"] = project.findProperty("buildTool") ?: ext["default_buildTool"]
ext["language"] = project.findProperty("language") ?: ext["default_language"]
ext["vertxVersion"] = project.findProperty("vertxVersion") ?: ext["default_vertxVersion"]
val dependencies: String? = project.findProperty("vertxDependencies") as String?
ext["vertxDependencies"] = if (dependencies.isNullOrBlank()) ext["default_vertxDependencies"] else dependencies?.split(",")?.map { it.trim() }
ext["archiveFormat"] = project.findProperty("archiveFormat") ?: ext["default_archiveFormat"]
ext["projectOutputDir"] = buildDir.resolve(ext["artifactId"] as String)

val clean = task("clean") {
  doLast {
    delete(ext["projectOutputDir"])
  }
}

val zip = task<Zip>("zip") {
  archiveName = "${ext["artifactId"]}.${ext["archiveFormat"]}"
  from(ext["projectOutputDir"])
  destinationDir = buildDir
}

val tar = task<Tar>("Tar") {
  archiveName = "${ext["artifactId"]}.${ext["archiveFormat"]}"
  from(ext["projectOutputDir"])
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
