dependencyResolutionManagement {
    repositories {
        // для доступа к community plugins:
        gradlePluginPortal()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        mavenCentral()
    }
}

rootProject.name = "my-build-logic"

//pluginManagement {
//    repositories {
//        // для доступа к community plugins:
//        gradlePluginPortal()
//        mavenCentral()
//        maven {
//            url = uri("https://plugins.gradle.org/m2/")
//        }
//    }
//}

include("java-plugins")
