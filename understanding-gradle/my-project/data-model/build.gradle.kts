plugins {
    `java-library`
    id("my-java-base")
}

dependencies {
    api("org.apache.commons:commons-lang3")
}

// перенесено в convention plugin:
//java {
//    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
//}