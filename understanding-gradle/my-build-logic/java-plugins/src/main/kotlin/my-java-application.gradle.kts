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

// свой аналог задачи distZip из плагина application
val packageAppTask = tasks.register<Zip>("packageApp") {
    group = myBuildGroup
    description = "Builds zip archive with application and run script"
    from(layout.projectDirectory.file("run.sh"))
    from(tasks.jar) {
        into("libs")
    }
    from(configurations.runtimeClasspath) {
        into("libs")
    }
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    archiveFileName.set("myApplication.zip")
}

tasks.build {
    dependsOn(packageAppTask)
}