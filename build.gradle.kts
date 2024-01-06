import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.vanniktech.maven.publish") version "0.27.0" apply false
}

buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.22.0")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
  }
}

allprojects {
  repositories {
    mavenCentral()
  }

  afterEvaluate {
    tasks
      .matching { task ->
        task is KotlinCompile && !task.name.contains("test", ignoreCase = true)
      }
      .configureEach {
        val EXPLICIT_API = "-Xexplicit-api=strict"
        this as KotlinCompile

        if (EXPLICIT_API !in kotlinOptions.freeCompilerArgs) {
          kotlinOptions.freeCompilerArgs += EXPLICIT_API
        }
      }

    tasks.withType<Test>().configureEach {
      outputs.upToDateWhen { false }
      useJUnitPlatform()
      testLogging {
        events = setOf(
          TestLogEvent.PASSED,
          TestLogEvent.SKIPPED,
          TestLogEvent.FAILED,
        )
      }
      afterSuite(
        KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
          if (desc.parent == null) { // will match the outermost suite
            val output = "Results: ${result.resultType} " +
              "(${result.testCount} tests, " +
              "${result.successfulTestCount} passed, " +
              "${result.failedTestCount} failed, " +
              "${result.skippedTestCount} skipped)"
            println(output)
          }
        })
      )
    }
  }
}
