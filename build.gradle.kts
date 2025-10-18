group "de.lukasbreuer"
version "1.0.0-SNAPSHOT"

tasks.register("build") {
  dependsOn(gradle.includedBuilds.map { it.task(":build") })
}