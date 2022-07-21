package myproject.tasks

import org.gradle.api.provider.Property

interface PackageAppExtension {
    val mainClass: Property<String>
}