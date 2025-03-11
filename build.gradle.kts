plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "com.imabanana80"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation("org.incendo:cloud-paper:2.0.0-beta.10")
    implementation("org.incendo:cloud-annotations:2.0.0")
    implementation("com.imabanana80:potassium:1.21.1-1.0-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    relocate("org.incendo.cloud", "com.imabanana80.blisstryout.shaded.cloud")
    relocate("com.imabanana80.potassium", "com.imabanana80.blisstryout.shaded.potassium")
    archiveClassifier.set("shadow")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}



