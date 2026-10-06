plugins { id("net.fabricmc.fabric-loom") }
version = "1.3.0+26.1.1.port.1"
repositories { mavenCentral() }
dependencies {
 minecraft("com.mojang:minecraft:26.1.1")
 implementation("net.fabricmc:fabric-loader:0.19.5")
 implementation("net.fabricmc.fabric-api:fabric-api:0.145.4+26.1.1")
}
java { toolchain.languageVersion = JavaLanguageVersion.of(25) }
