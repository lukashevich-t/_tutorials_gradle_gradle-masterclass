// Репозитории для плагинов самого gradle:
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
    includeBuild("../my-build-logic")
}

// Репозитории для всех подпроектов (для общих зависимостей):
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
//  includeBuild("../my-other-project")
}

rootProject.name = "my-project"

// settings plugins (для совместной конфигурации разных билдов)
// plugins {
//   id("...")
// }

include("app", "business-logic", "data-model")

//rootDir.listFiles().filter {
//    it.isDirectory
//            && !it.isHidden
//            && !it.name.startsWith(".")
//            && it.name != "gradle"
//            && it.name != "my-build-logic"
//}.forEach {
//    include(it.name)
//}