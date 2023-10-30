plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish")
}

dependencies {
  api(project(":ktor-client-logging-json"))
  api("com.google.code.gson:gson:2.10.1")

  testImplementation("io.ktor:ktor-client-core:2.3.5")
  testImplementation("io.ktor:ktor-client-mock:2.3.5")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
  testImplementation("io.kotest:kotest-assertions-core-jvm:5.7.2")
  testImplementation(kotlin("test"))
}
