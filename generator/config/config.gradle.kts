val configDir = "${project.projectDir}/config/files"

val ext:ExtraPropertiesExtension by extensions
val projectOutputDir: File by ext

val dotFiles = task<Copy>("dotFiles") {
  from(configDir)
  include("_*")
  rename { filename -> filename.replaceFirst('_', '.') }
  into(projectOutputDir)
}

val configFiles = task<Copy>("configFiles") {
  dependsOn(dotFiles)
  from(configDir)
  exclude("_*")
  into(projectOutputDir)
}


tasks["assembleVertxProject"].dependsOn(configFiles)
