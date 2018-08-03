import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  kotlin("jvm") version "1.2.51"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("stdlib"))

  testImplementation(gradleTestKit())
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.2.0")
  testImplementation("org.assertj:assertj-core:3.10.0")
  testImplementation("org.zeroturnaround:zt-exec:1.10")
  testImplementation("org.zeroturnaround:zt-process-killer:1.8")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.2.0")
}

configure<JavaPluginConvention> {
  sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
  useJUnitPlatform {
    var include = project.findProperty("includeTags") as String?
    if (!include.isNullOrBlank()) {
      val includeTags = include!!.split(",").map { it.trim() }
      includeTags(*includeTags.toTypedArray())
    }
    var exclude = project.findProperty("excludeTags") as String?
    if (!exclude.isNullOrBlank()) {
      val excludeTags = exclude!!.split(",").map { it.trim() }
      excludeTags(*excludeTags.toTypedArray())
    }
  }
  testLogging {
    events("PASSED", "FAILED", "SKIPPED")
    showStandardStreams = true
  }
}
