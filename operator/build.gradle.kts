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
  testImplementation(platform("org.junit:junit-bom:6.0.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  implementation("de.lukasbreuer:core:1.0.0-SNAPSHOT")

  implementation("com.google.inject:guice:7.0.0")

  implementation("com.google.guava:guava:33.6.0-jre")

  implementation("org.projectlombok:lombok:1.18.46")
  annotationProcessor("org.projectlombok:lombok:1.18.46")
  testImplementation("org.projectlombok:lombok:1.18.46")
  testAnnotationProcessor("org.projectlombok:lombok:1.18.46")

  implementation("org.json:json:20251224")
  implementation("commons-io:commons-io:2.22.0")

  implementation("io.netty:netty-all:4.2.12.Final")

  implementation("de.lukasbreuer:signar:1.0.0-SNAPSHOT")
}

tasks.test {
  useJUnitPlatform()
}