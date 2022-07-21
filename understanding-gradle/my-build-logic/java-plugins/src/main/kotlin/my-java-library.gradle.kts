// Имя файла станет идентификатором плагина
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("my-java-base")
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
}
