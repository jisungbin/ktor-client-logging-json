plugins {
  kotlin("jvm")
}

kotlin {
  explicitApi()
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  implementation("io.ktor:ktor-client-core:2.3.5")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

  testImplementation("io.ktor:ktor-client-mock:2.3.5")
  testImplementation("com.squareup.moshi:moshi:1.14.0")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
  testImplementation(kotlin("test"))
}

apply(plugin = "kotlinx-atomicfu")
