// Имя файла станет идентификатором плагина
import myproject.tasks.GenerateStartScript
import myproject.tasks.PackageAppExtension

plugins {
    id("my-java-base")
    id("application")
}

val myBuildGroup = "my project build"
// Переместить существующую задачу в нашу "определённую группу"
tasks.named("run") {
    group = myBuildGroup
}

val packageAppExtension = extensions.create<PackageAppExtension>("packageApp")

val generateStartScript = tasks.register<GenerateStartScript>("generateStartScript") {
    // Конфигурация задачи через расширение
    mainClass.convention(packageAppExtension.mainClass)
    // Конфигурация задачи напрямую (без использования расширения)
    scriptFile.set(layout.buildDirectory.file("run.sh"))
}

// свой аналог задачи distZip из плагина application
val packageAppTask = tasks.register<Zip>("packageApp") {
    group = myBuildGroup
    description = "Builds zip archive with application and run script"
    from(generateStartScript)
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