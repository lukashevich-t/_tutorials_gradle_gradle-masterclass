// Имя файла станет идентификатором плагина
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("my-java-base")
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.apache.commons:commons-lang3")
}
