// Имя файла станет идентификатором плагина
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("my-java-base")
    id("application")
}

val myBuildGroup = "my project build"
// Переместить существующую задачу в нашу "определённую группу"
tasks.named("run") {
    group = myBuildGroup
}
