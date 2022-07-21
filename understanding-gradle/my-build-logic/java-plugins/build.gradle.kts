plugins {
    `kotlin-dsl` // for kotlin DSL
    // id("groovy-gradle-plugin") // for Groovy DSL
}

dependencies {
    // Нужные зависимости для работы плагина kotlin-dsl:
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin") // версия определится автоматически
}