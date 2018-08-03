import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  compileOnly(gradleKotlinDsl())
  compile(kotlin("stdlib-jdk8"))
  compile("org.freemarker:freemarker:2.3.28")
  testCompile(gradleTestKit())

}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}
