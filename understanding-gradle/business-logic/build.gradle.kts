plugins {
    // core plugin (входит в состав gradle)
//     id("java-library")
    // community plugin
//     id("org.jetbrains.kotlin.jvm") version "1.5.21"
    /**
     * convention plugins. Ихисходники лежат в локально в buildSrc или в произвольной папке, путь к которой указывается в settings.gradle[.kts]:.
     * pluginManagement { includeBuild("../my-build-logic") }.
     * Служат для шаринга конфигураций сборки или какой-то логики сборки между подпроектами.
     */
    id("my-java-library")
}

// Плагины предоставляют "расширения" (extensions). Доступ к расширению плагина java-library (уже перенесено в convention plugin):
//java {
//    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
//}

dependencies {
//    implementation(project(":data-model"))
    // Перенесено в convention plugin:
//    implementation("org.apache.commons:commons-lang3:3.12.0")
}