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
}