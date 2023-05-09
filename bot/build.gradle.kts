plugins {
    id("java")
}

group = "ru.rachie.bot"
version = "1.0.0"

val yamlVersion = "2.15.0"
val mindustryVersion = "v143.1"

repositories {
    mavenCentral()
    maven("https://www.jitpack.io")
}

dependencies {
    implementation(project(":api"))

    implementation("com.discord4j:discord4j-core:3.2.4")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$yamlVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$yamlVersion")

    implementation("com.github.Anuken.Arc:arc-core:$mindustryVersion")
    implementation("com.github.Anuken.MindustryJitpack:core:$mindustryVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    targetCompatibility = "20"
    sourceCompatibility = "20"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "$group.Main"
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}