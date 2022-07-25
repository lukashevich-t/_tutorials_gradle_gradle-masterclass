// Имя файла станет идентификатором плагина
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
    id("checkstyle")
}

group = "org.example.my-app"

// Ограничить вывод команды tasks определённой группой
val myBuildGroup = "my project build"
tasks.named<TaskReportTask>("tasks") {
    displayGroup = myBuildGroup
}

// Переместить существующую задачу в нашу "определённую группу"
tasks.build {
    group = myBuildGroup
}

// Переместить существующую задачу в нашу "определённую группу"
tasks.check {
    group = myBuildGroup
    description = "Runs checks (including tests)."
}

// сделать свою lifecycle task:
tasks.register("qualityCheck") {
    group = myBuildGroup
    description = "Runs checks (excluding tests)."
    dependsOn(tasks.classes, tasks.checkstyleMain)
//    dependsOn(tasks.testClasses, tasks.checkstyleTest)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

// задать версии зависимостей в одном месте:
dependencies.constraints {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.google.errorprone:error_prone_annotations:2.9.0")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.slf4j:slf4j-simple:1.7.32")
}

checkstyle {
    // the only version compatible with gradle 7.5. In addition, checkstyle config must be downloaded
    // from https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml
    // and put into [root]/config/checkstyle/checkstyle.xml
    toolVersion = "10.3.1"
}