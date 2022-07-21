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