// Репозитории для плагинов самого gradle:
pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
  }
//  includeBuild("../my-build-logic")
}

// Репозитории для всех подпроектов (для общих зависимостей):
dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
//  includeBuild("../my-other-project")
}

rootProject.name = "understanding-gradle"

// settings plugins (для совместной конфигурации разных билдов)
// plugins {
//   id("...")
// }

//include("app")
//include("business-logic")
//include("data-model")

rootDir.listFiles().filter { it.isDirectory && !it.isHidden && !it.name.startsWith(".") && it.name != "gradle" }.forEach {
  include(it.name)
}