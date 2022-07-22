## Что это
- мой прогресс прослушивания курса [The Gradle Masterclass](https://www.udemy.com/course/gradle-masterclass/). 
- [туториал](https://www.youtube.com/playlist?list=PLWQK2ZdV4Yl2k2OmC_gsjDpdIBTN0qqkE) ([github](https://github.com/jjohannes/understanding-gradle)) (папка *understanding-gradle*)
- возможно, прочие заметки

## API
- [org.gradle.api.Script](https://docs.gradle.org/current/javadoc/org/gradle/api/Script.html)
- [org.gradle.api.Project](https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html)
- [org.gradle.api.invocation.Gradle](https://docs.gradle.org/current/javadoc/org/gradle/api/invocation/Gradle.html)
- [org.gradle.api.initialization.Settings](https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/Settings.html)
- [org.gradle.api.Task](https://docs.gradle.org/current/javadoc/org/gradle/api/Task.html)
- [org.gradle.api.Action](https://docs.gradle.org/current/javadoc/org/gradle/api/Action.html)
## Свойства
папки 020-020
### Script interface
Встроенные:
   - logger
### Gradle interface
Свойства для Gradle задаются:
- программно через объект-расширение **gradle.ext**: `gradle.ext.newProp = "newValue"`
Предопределенные свойства:
- gradleVersion
- gradleUserHomeDir
- gradleHomeDir
- parent
- startParameter
### Project interface
Свойства для проекта задаются:
- в **$GRADLE_USER_HOME/gradle.properties**
- в **<корень проекта>/gradle.properties**.
- программно через объект-расширение **project.ext**: `project.ext.newProp = "newValue"`
- через командную строку: **-Pprop=value**

Получить значение свойства:
- через ссылку на project: `project.'x.y.z'`
- через ссылку на project.ext: `project.ext.getProperty('x.y.z')`
- просто по имени свойства: `propName`, `"$propName"`

Проверить наличие свойства: `project.hasProperty('propName')`

Предопределенные свойства:
- projectDir
- buildFile

## Tasks
Папки 
- 023 (создание)
- 025, 026 (зависимости)
- 027 (условные зависимости)
- 029 ([Хуки жизненного цикла](https://docs.gradle.org/current/javadoc/org/gradle/api/execution/TaskExecutionGraph.html))

Задачи делятся на lifecycle tasks и actionable tasks. Вторые присоединяются к первым. Например при подключении плагина `java` actionable task `compileJava` добавляется в зависимости к lifecycle task `build`.

Задачи создаются на этапе конфигурации, а выполняются на этапе выполнения.

создать задачу:
```gradle
// Groovy:
task name1 {}
task("name2") {}

// Kotlin:
tasks.register("name") {}
```

Доступ к задачам:
```
// Groovy:
logger.quiet hi.toString()
logger.quiet tasks['hi'].toString()
logger.quiet project.tasks[6].toString()
// Kotlin:
tasks.named("run")
tasks.named<TaskReportTask>("tasks")
tasks.build
```

Зависимости между задачами могут быть и между подпроектами и даже проектами в другом месте, например:
```gradle
// root build.gradle.kts
tasks.register("qualityCheckAll") {
    // Задача в другом проекте этой же сборки:
    dependsOn(subprojects.map {":${it.name}:qualityCheck"})
    // Задача в "соседней" сборке (см. includeBuild("..") в settings.gradle[.kts]):
    dependsOn(gradle.includedBuilds.map {it.task(":checkAll")})
}
```

У задач есть входы (inputs) и выходы (outputs). Входы:
- файлы
- коллекции файлов
- папки
- Configuration data (?)

Выходы: набор файлов и/или папок.

Если выход одной задачи служит входом для другой, то автоматически устанавливается зависимость между задачами. Также, входы и выходы участвуют в инкрементной сборке, т.е. если со времени последнего выполнения входы и выходы не изменились и не были удалены, то задача не будет выполняться.

Примеры указания входов:
```gradle
// Groovy:
jar {
  from {
    def jarContent = project.configurations.runtimeClasspath.collect { File file ->  project.zipTree(file) }
    jarContent += '/home/tim/.zshrc1'
    return jarContent
  }
}
task deployToTomcat(type: Copy) {
  from war.archivePath
  from war
}

// Kotlin:
tasks.register<Zip>("packageApp") {
    from(layout.projectDirectory.file("run.sh"))
    from(tasks.jar) {
        into("libs")
    }
    from(configurations.runtimeClasspath) {
        into("libs")
    }
}
```

Задача, вынесенная в отдельные классы:
```kotlin
// PackageAppExtension.kt
package myproject.tasks

import org.gradle.api.provider.Property

interface PackageAppExtension { val mainClass: Property<String>}
```

```kotlin
// GenerateStartScript.kt
package myproject.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.lang.UnsupportedOperationException
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission.GROUP_READ
import java.nio.file.attribute.PosixFilePermission.GROUP_WRITE
import java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE
import java.nio.file.attribute.PosixFilePermission.OWNER_READ
import java.nio.file.attribute.PosixFilePermission.OWNER_WRITE

// Класс абстрактный, потому что gradle его унаследует и создаст реальный
abstract class GenerateStartScript : DefaultTask() {
    //    @get:InputFile
    //    @get:InputFiles
    @get:Input
    abstract val mainClass: Property<String>

    @get:OutputFile
    abstract val scriptFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val main = mainClass.get()
        val out = scriptFile.get().asFile // java.io.File
        val script = "java -cp libs/* $main\n"
        out.writeText(script)
        try {
            Files.setPosixFilePermissions(
                out.toPath(), setOf(
                    OWNER_READ, OWNER_WRITE, OWNER_EXECUTE, GROUP_READ, GROUP_WRITE
                )
            )
        } catch (ignored: UnsupportedOperationException) {}
    }
}
```
```kotlin
// my-java-application.gradle.kts
val packageAppExtension = extensions.create<PackageAppExtension>("packageApp")

val generateStartScript = tasks.register<GenerateStartScript>("generateStartScript") {
    // Конфигурация задачи через расширение
    mainClass.convention(packageAppExtension.mainClass)
    // Конфигурация задачи напрямую (без использования расширения)
    scriptFile.set(layout.buildDirectory.file("run.sh"))
}

val packageAppTask = tasks.register<Zip>("packageApp") {
    from(generateStartScript)
}
```
```gradle
// build.gradle.kts
// Конфигурация задачи generateStartScript через расширение
packageApp {
    mainClass.set("MyApplication1")
}

// Конфигурация задачи напрямую (без использования расширения)
tasks.generateStartScript {
    mainClass.set("MyApplication")
}
```

## Java plugin https://docs.gradle.org/current/userguide/java_plugin.html
Папки
- 032 (console application)
- 036 (fat jar и свое содержимое для jar-файла)

## war plugin https://docs.gradle.org/current/userguide/java_plugin.html
Приложение рассчитано на tomcat 9. на версии 10 не работает

Ссылки
- [war plugin](https://docs.gradle.org/current/userguide/war_plugin.html)
- [war task](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.War.html)

Папки
- 038

## dependencies
Папки
- 041

`gradle dependencies` - смотреть на разделы:
  - implementation - там прямые зависимости
  - compileClasspath - там прямые и транзитивные зависимости
  - runClasspath - там прямые и транзитивные зависимости, кроме помеченных как *compileOnly*. Т.е. это те зависимости, которые надо притянуть в свой war/jar.

Зависимости могут быть версионированными или нет. Для подключения последних не надо указывать номер версии:
```gradle
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    api("org.example:my-data-model")
}
```
*implementation*, *api* - это конфигурации. Это общее, достаточно сложное понятие, пришло из Ivy. Можно представить их как "корзины", в которые кладутся зависимости и которые имеют определенные свойства и область в месте их применения. Конфигурации добавляются многими плагинами; так, *api* появляется при применении плагина "java-libary".

Зависимости могут находиться извне (во внешнем репозитории); в том же билде; в другом билде (*includeBuild* в settings.gradle). Желательно прописать включение самого нашего билда в поиск зависимостей (для обратной совместимости):
```gradle
// settings.gradle.kts
dependencyResolutionManagement {
    includeBuild("../my-other-project")
    includeBuild(".")
}
```

Плагин *java-library* имеет 5 конфигураций зависимостей:
- implementation - зависимость доступна при компиляции и выполнении
- api - зависимость доступна при компиляции и выполнении, плюс видна потребителям нашей библиотеки. Т.е. другие компоненты, зависящие от нашей библиотеки, будут видеть эту зависимость на этапе компиляции, т.к. это часть публичного API нашей библиотеки.
- compileOnly - зависимость доступна при компиляции, но не при выполнении. Обычно используется для подключения аннотаций, не нужных при выполнении. Например, `jetbrains:annotations` или `com.google.errorprone:error_prone_annotations`, которые используются IDE или утилитами для дополнительного контроля кода. Еще одно применение - если зависимость предоставляется, например, контейнером приложений.
- compileOnlyApi - то же, что *compileOnly*, но распространяется потребителям библиотеки
- runtimeOnly - зависимость видна при выполнении, но не при компиляции

## Multi-project builds
Папки
- 044-allAboutNumbers

Базовый проект состоит из:
- файла build.gradle в корне с базовым содержимым:
```gradle
subprojects {
    apply plugin: 'java'
}
```
- файла settings.gradle в корне:
```gradle
include 'pickerNumberApplication1', 'plusplus', 'randomNumberGen1'

rootProject.name = 'allAboutNumbers'
```
- пустых файлов build.gradle в папках подпроектов

Можно изменить имена сборочных файлов для подпроектов, чтобы не путаться:
```gradle
// settings.gradle
rootProject.children.each { subproject -> 
     subproject.buildFileName = "${subproject.name}.gradle" 
}
```
## Multi-project web app
Папки:
- 048-academy

## Полезные плагины
### project-report
[Полезный плагин](https://docs.gradle.org/current/userguide/project_report_plugin.html) для построения отчетов:
```gradle
plugins {
  id 'project-report'
}
// или так:
apply plugin: 'project-report'
```
Полезные задачи в нём:
- dependencyReport - то же, что `gradle dependencies`, только пишет в файл
- :htmlDependencyReport - то же, что `gradle dependencies`, только пишет в файл в формате html
- propertyReport - пишет в файл все свойства проекта
- taskReport - то же, что `gradle tasks`
- projectReport - предыдущие 4 задачи одним махом

### FindBugs
Отсутствует в версии gradle 7.5

```gradle
// build.gradle
apply plugin: 'findbugs'
```

### SpotBugs
[SpotBugs](https://github.com/spotbugs/spotbugs-gradle-plugin) Заменил плагин FindBugs.

Подключение к дочернему проекту:
```gradle
// build.gradle дочернего проекта
plugins {
  id "com.github.spotbugs" version "5.0.9"
}
```

Подключение к корневому проекту 1:
```gradle
// build.gradle корневого проекта
plugins {
  id "com.github.spotbugs" version "5.0.9"
}
subprojects {
	apply plugin: "com.github.spotbugs"
}
```

Подключение к корневому проекту 2:
```gradle
// build.gradle корневого проекта
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.github.spotbugs.snom:spotbugs-gradle-plugin:5.0.9"
  }
}
subprojects {
	apply plugin: "com.github.spotbugs"
}
```

Конфигурация:
```gradle
// build.gradle корневого или дочернего проекта
//subprojects {
  spotbugs {
      ignoreFailures = true
  }

  spotbugsMain {
      reports {
          html {
              required = true
              outputLocation = file("$buildDir/reports/spotbugs/main/spotbugs.html")
              stylesheet = 'fancy-hist.xsl'
          }
      }
  }
//}
```

Вызывать задачи `spotbugsMain`, `spotbugsTest` или `check`.
### PMD
[Статический анализатор кода](https://docs.gradle.org/current/userguide/pmd_plugin.html)

Подключение к корневому или дочернему проекту:
```gradle
//subprojects {
  apply plugin: 'pmd'
//}
```
Конфигурация:
```gradle
// subprojects{
pmd {
    ignoreFailures = true
    pmdTest.enabled= false
		ruleSets = [
			"category/java/errorprone.xml",
			"category/java/multithreading.xml",
			"category/java/bestpractices.xml",
			"category/java/codestyle.xml",
			"category/java/performance.xml",
			"category/java/design.xml",
			"category/java/documentation.xml"
		]
}

tasks.withType(Pmd){
    reports{
        xml.enabled=true
        html.enabled=true
    }
}
//}
```
Задачи: `check`, `pmdTest`, `pmdMain`

## Kotlin DSL

Создать fat jar:
```gradle
val jar: Jar by tasks
jar.apply {
    baseName = "${project.name}-all"  
    manifest {
        attributes["Implementation-Title"] = "Jar File -all Example"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "com.denofprogramming.random.App"
    }
    from(configurations["runtimeClasspath"].map({file -> project.zipTree(file) }))
}
```

## snippets
### создать wrapper 1:
`gradle wrapper --distribution-type=bin --gradle-version=7.5`

### создать wrapper 2:
```gradle
// build.gradle
wrapper {
  gradleVersion='7.5'
  distributionType='bin'
}
```
`gradle wrapper`

wrapper не проверяет, существует ли запрошенная версия gradle.

### получить помощь:
```
gradle help
gradle help --task wrapper
```

### Подключить все вложенные папки как подпроекты:
```gradle
// settings.gradle
rootDir.listFiles().filter {it.isDirectory && !it.isHidden }.forEach {
  include(it.name)
}
```

## опции командной строки
### Включить build cache
`--build-cache`
### изменить вывод результатов
`--console=[plain|auto|rich|verbose`

plain предназначен в основном для вывода в файл, но может быть полезен и так.