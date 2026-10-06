plugins {
    id("net.fabricmc.fabric-loom") version "1.16.3"
}

group = "dev.galena"
version = property("mod_version")!!

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") }
        filter { includeGroup("maven.modrinth") }
    }
}

dependencies {
    implementation(project(":hats"))
    include(project(":hats"))
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    implementation("maven.modrinth:forge-config-api-port:RXbL43Lt")
    include("maven.modrinth:forge-config-api-port:RXbL43Lt")
    compileOnly("maven.modrinth:jei:EOREyJjJ")
    if (providers.gradleProperty("testJei").isPresent) runtimeOnly("maven.modrinth:jei:EOREyJjJ")
}

loom { accessWidenerPath = file("src/main/resources/nirvana.accesswidener") }

fabricApi {
    configureTests {
        createSourceSet = true
        modId = "nirvana-port-test"
        enableGameTests = false
        enableClientGameTests = true
        eula = false
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") { expand("version" to project.version) }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "1000"))
}

tasks.jar {
    from(rootProject.file("../LICENSE.md"))
    archiveBaseName = "nirvana-fabric"
}
