plugins {
    // добавляет lifecycle tasks:
//    id("base")
    id("my-java-application")
    id("application")
}

application {
    mainClass.set("MyApplication")
}

dependencies {
    implementation(project(":business-logic"))
}

// перенесено в convention plugin:
//java {
//    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
//}