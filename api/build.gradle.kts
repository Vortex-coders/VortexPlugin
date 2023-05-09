plugins {
    id("java")
}

group = "ru.rachie.api"
version = "1.0.0"

val mindustryVersion = "v143.1"

repositories {
    mavenCentral()
    maven("https://www.jitpack.io")
}

dependencies {
    implementation("com.esotericsoftware:kryo:5.5.0")

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
