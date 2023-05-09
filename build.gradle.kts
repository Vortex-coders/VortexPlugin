plugins {
    id("java")
}

group = "ru.rachie.vortex"
version = "1.0.0"

val yamlVersion = "2.15.0"
val mindustryVersion = "v143.1"

repositories {
    mavenCentral()
    maven("https://www.jitpack.io")
}

dependencies {
    implementation(project(":api"))

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$yamlVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$yamlVersion")

    compileOnly("com.github.Anuken.Arc:arc-core:$mindustryVersion")
    compileOnly("com.github.Anuken.MindustryJitpack:core:$mindustryVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    targetCompatibility = "20"
    sourceCompatibility = "20"
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}