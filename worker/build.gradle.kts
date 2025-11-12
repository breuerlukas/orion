plugins {
  id("java")
}

group = "de.lukasbreuer"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven {
    url = uri("https://maven.pkg.github.com/breuerlukas/signar")
    credentials {
      username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_USERNAME")
      password = project.findProperty("gpr.token")?.toString() ?: System.getenv("GITHUB_TOKEN")
    }
  }
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:6.0.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  implementation("de.lukasbreuer:core:1.0.0-SNAPSHOT")

  implementation("com.google.inject:guice:7.0.0")

  implementation("com.google.guava:guava:33.5.0-jre")

  implementation("org.projectlombok:lombok:1.18.42")
  annotationProcessor("org.projectlombok:lombok:1.18.42")
  testImplementation("org.projectlombok:lombok:1.18.42")
  testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

  implementation("org.json:json:20250517")
  implementation("commons-io:commons-io:2.21.0")

  implementation("io.netty:netty-all:4.2.7.Final")

  implementation("de.lukasbreuer:signar:1.0.0-SNAPSHOT")
}

tasks.test {
  useJUnitPlatform()
}