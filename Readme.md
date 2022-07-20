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

Задачи создаются на этапе конфигурации, а выполняются на этапе выполнения.

Доступ к задачам:
```
logger.quiet hi.toString()
logger.quiet tasks['hi'].toString()
logger.quiet project.tasks[6].toString()
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

dependency scope "api" появляется только при применении плагина "java-libary". Этот scope распространяет зависимость дальше.

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
