plugins {
    `java-library`
}

dependencies {
    api("org.apache.commons:commons-lang3:3.9")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}