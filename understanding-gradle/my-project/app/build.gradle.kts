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
    implementation("org.example.my-app:business-logic") // или так: implementation(project(":business-logic"))
    runtimeOnly("org.slf4j:slf4j-simple")
}

// перенесено в convention plugin:
//java {
//    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
//}