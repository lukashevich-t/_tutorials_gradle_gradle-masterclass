plugins {
    // core plugin (входит в состав gradle)
    id("java-library")
    // community plugin
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    // convention plugin (исходники лежат в локально). Служат для шаринга конфигураций сборки
//    id("my-java-library")
}

// Плагины предоставляют "расширения" (extensions). Доступ к расширению плагина java-library:
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

dependencies {
//    implementation(project(":data-model"))
    implementation("org.apache.commons:commons-lang3:3.9")
}