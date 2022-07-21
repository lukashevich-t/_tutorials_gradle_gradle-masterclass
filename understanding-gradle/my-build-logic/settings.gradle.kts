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
