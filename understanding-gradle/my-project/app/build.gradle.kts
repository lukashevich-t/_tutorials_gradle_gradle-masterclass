plugins {
    // добавляет lifecycle tasks:
//    id("base")
    id("my-java-application")
    id("application")
}

application {
    mainClass.set("MyApplication")
}

// Конфигурация задачи generateStartScript через расширение
packageApp {
    mainClass.set("MyApplication1")
}

// Конфигурация задачи напрямую (без использования расширения)
tasks.generateStartScript {
    mainClass.set("MyApplication")
}

dependencies {
    implementation(project(":business-logic"))
}

// перенесено в convention plugin:
//java {
//    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
//}