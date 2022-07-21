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

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}