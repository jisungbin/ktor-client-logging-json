rootProject.name = "ktor-client-logging-json"

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

buildCache {
  local {
    removeUnusedEntriesAfterDays = 7
  }
}

include(
  ":ktor-client-logging-json",
  ":ktor-client-logger-gson",
  ":ktor-client-logger-moshi",
  ":ktor-client-logger-jackson",
)