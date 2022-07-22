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

checkstyle {
    // the only version compatible with gradle 7.5. In addition, checkstyle config must be downloaded
    // from https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml
    // and put into [root]/config/checkstyle/checkstyle.xml
    toolVersion = "10.3.1"
}