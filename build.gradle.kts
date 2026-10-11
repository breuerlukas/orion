group "de.lukasbreuer"
version "1.0.0"

tasks.register("build") {
  dependsOn(gradle.includedBuilds.map { it.task(":build") })
}