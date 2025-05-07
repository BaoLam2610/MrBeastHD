package com.lambao.base.extension

import android.content.Context
import androidx.annotation.WorkerThread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

/**
 * Creates a file in the internal storage directory ([Context.filesDir]).
 *
 * @param fileName The name of the file (e.g., "data.txt").
 * @param createParentDirs Whether to create parent directories if they don't exist (default: true).
 * @return The created [File] object, or null if creation fails.
 *
 * **Note**: No permissions are required for internal storage.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * file?.writeText("Hello, World!")
 * ```
 */
@WorkerThread
fun Context.getInternalFile(fileName: String, createParentDirs: Boolean = true): File? {
    return try {
        File(filesDir, fileName).apply {
            if (createParentDirs) {
                parentFile?.mkdirs()
            }
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Creates a file in the cache directory ([Context.cacheDir]).
 *
 * @param fileName The name of the file (e.g., "temp.txt").
 * @param createParentDirs Whether to create parent directories if they don't exist (default: true).
 * @return The created [File] object, or null if creation fails.
 *
 * **Note**: Cache files may be deleted by the system when storage is low.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getCacheFile("temp.txt")
 * file?.writeText("Temporary data")
 * ```
 */
@WorkerThread
fun Context.getCacheFile(fileName: String, createParentDirs: Boolean = true): File? {
    return try {
        File(cacheDir, fileName).apply {
            if (createParentDirs) {
                parentFile?.mkdirs()
            }
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Creates a file in a subdirectory of the internal storage directory.
 *
 * @param subdir The subdirectory name.
 * @param fileName The name of the file.
 * @param createParentDirs Whether to create parent directories if they don't exist (default: true).
 * @return The created [File] object, or null if creation fails.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalSubdirFile("images", "profile.jpg")
 * file?.writeText("Image data")
 * ```
 */
@WorkerThread
fun Context.getInternalSubdirFile(subdir: String, fileName: String, createParentDirs: Boolean = true): File? {
    return try {
        val subdirFile = File(filesDir, subdir).apply {
            if (createParentDirs) mkdirs()
        }
        File(subdirFile, fileName)
    } catch (e: Exception) {
        null
    }
}

/**
 * Creates a file in a subdirectory of the cache directory.
 *
 * @param subdir The subdirectory name.
 * @param fileName The name of the file.
 * @param createParentDirs Whether to create parent directories if they don't exist (default: true).
 * @return The created [File] object, or null if creation fails.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getCacheSubdirFile("temp_images", "thumb.jpg")
 * file?.writeText("Thumbnail data")
 * ```
 */
@WorkerThread
fun Context.getCacheSubdirFile(subdir: String, fileName: String, createParentDirs: Boolean = true): File? {
    return try {
        val subdirFile = File(cacheDir, subdir).apply {
            if (createParentDirs) mkdirs()
        }
        File(subdirFile, fileName)
    } catch (e: Exception) {
        null
    }
}

/**
 * Reads the content of the file as a string.
 *
 * @param charset The charset to use (default: UTF-8).
 * @return The file content as a string, or null if an error occurs (e.g., file doesn't exist).
 * @throws SecurityException If the file cannot be read due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val content = file?.readText() // e.g., "Hello, World!"
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.readText(charset: Charset = Charsets.UTF_8): String? {
    return try {
        inputStream().bufferedReader(charset).use { it.readText() }
    } catch (e: IOException) {
        null
    }
}

/**
 * Reads the content of the file as a list of lines.
 *
 * @param charset The charset to use (default: UTF-8).
 * @return A list of lines, or empty list if an error occurs (e.g., file doesn't exist).
 * @throws SecurityException If the file cannot be read due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val lines = file?.readLines() // e.g., ["Line 1", "Line 2"]
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.readLines(charset: Charset = Charsets.UTF_8): List<String> {
    return try {
        bufferedReader(charset).useLines { it.toList() }
    } catch (e: IOException) {
        emptyList()
    }
}

/**
 * Reads the content of the file as a byte array.
 *
 * @return The file content as a byte array, or null if an error occurs (e.g., file doesn't exist).
 * @throws SecurityException If the file cannot be read due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("image.jpg")
 * val bytes = file?.readBytes()
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.readBytes(): ByteArray? {
    return try {
        FileInputStream(this).use { it.readBytes() }
    } catch (e: IOException) {
        null
    }
}

/**
 * Writes text to the file.
 *
 * @param text The text to write.
 * @param charset The charset to use (default: UTF-8).
 * @param append Whether to append to the file or overwrite it (default: false).
 * @return True if writing succeeds, false otherwise.
 * @throws SecurityException If the file cannot be written due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val success = file?.writeText("Hello, World!")
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.writeText(text: String, charset: Charset = Charsets.UTF_8, append: Boolean = false): Boolean {
    parentFile?.mkdirs()
    return try {
        FileOutputStream(this, append).use { it.write(text.toByteArray(charset)) }
        true
    } catch (e: IOException) {
        false
    }
}

/**
 * Writes a byte array to the file.
 *
 * @param bytes The byte array to write.
 * @param append Whether to append to the file or overwrite it (default: false).
 * @return True if writing succeeds, false otherwise.
 * @throws SecurityException If the file cannot be written due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.bin")
 * val bytes = byteArrayOf(0x01, 0x02, 0x03)
 * val success = file?.writeBytes(bytes)
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.writeBytes(bytes: ByteArray, append: Boolean = false): Boolean {
    parentFile?.mkdirs()
    return try {
        FileOutputStream(this, append).use { it.write(bytes) }
        true
    } catch (e: IOException) {
        false
    }
}

/**
 * Writes an InputStream to the file.
 *
 * @param inputStream The InputStream to write.
 * @param append Whether to append to the file or overwrite it (default: false).
 * @param bufferSize Size of the buffer used for copying (default: 8192 bytes).
 * @return True if writing succeeds, false otherwise.
 * @throws SecurityException If the file cannot be written due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val inputStream = context.assets.open("sample.txt")
 * val success = file?.writeInputStream(inputStream)
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.writeInputStream(inputStream: InputStream, append: Boolean = false, bufferSize: Int = 8192): Boolean {
    parentFile?.mkdirs()
    return try {
        FileOutputStream(this, append).use { output ->
            inputStream.use { it.copyTo(output, bufferSize) }
        }
        true
    } catch (e: IOException) {
        false
    }
}

/**
 * Copies the file to a destination file.
 *
 * @param destination The destination File.
 * @param overwrite Whether to overwrite the destination if it exists (default: false).
 * @param bufferSize Size of the buffer used for copying (default: 8192 bytes).
 * @return True if copying succeeds, false otherwise.
 * @throws SecurityException If the file cannot be read or written due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val source = context.getInternalFile("data.txt")
 * val dest = context.getInternalFile("backup.txt")
 * val copied = source?.copyTo(dest) ?: false
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.copyTo(destination: File, overwrite: Boolean = false, bufferSize: Int = 8192): Boolean {
    if (!exists()) return false
    if (destination.exists() && !overwrite) return false
    return try {
        destination.parentFile?.mkdirs()
        FileInputStream(this).use { input ->
            FileOutputStream(destination).use { output ->
                input.copyTo(output, bufferSize)
            }
        }
        true
    } catch (e: IOException) {
        false
    }
}

/**
 * Moves the file to a destination file (copy and delete source).
 *
 * @param destination The destination File.
 * @param overwrite Whether to overwrite the destination if it exists (default: false).
 * @return True if moving succeeds, false otherwise.
 * @throws SecurityException If the file cannot be read, written, or deleted due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val source = context.getInternalFile("data.txt")
 * val dest = context.getInternalFile("new_data.txt")
 * val moved = source?.moveTo(dest) ?: false
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.moveTo(destination: File, overwrite: Boolean = false): Boolean {
    if (!exists()) return false
    if (destination.exists() && !overwrite) return false
    return try {
        if (copyTo(destination, overwrite)) {
            delete()
        } else {
            false
        }
    } catch (e: IOException) {
        false
    }
}

/**
 * Gets the file extension (without the dot).
 *
 * @return The file extension (e.g., "txt"), or empty string if no extension or for files like `.gitignore`.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val ext = file?.extension() // "txt"
 * ```
 */
fun File.extension(): String {
    val name = name
    val dotIndex = name.lastIndexOf('.')
    return if (dotIndex > 0 && dotIndex < name.length - 1) {
        name.substring(dotIndex + 1)
    } else {
        ""
    }
}

/**
 * Gets the file name without the extension.
 *
 * @return The file name without extension, or the full name if no extension.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val name = file?.nameWithoutExtension() // "data"
 * ```
 */
fun File.nameWithoutExtension(): String {
    val dotIndex = name.lastIndexOf('.')
    return if (dotIndex > 0) name.substring(0, dotIndex) else name
}

/**
 * Checks if the file has a specific extension (case-insensitive).
 *
 * @param extension The extension to check (e.g., "txt"). Can include or exclude the dot.
 * @return True if the file has the specified extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val isTxt = file?.hasExtension("txt") // true
 * ```
 */
fun File.hasExtension(extension: String): Boolean {
    val cleanExtension = extension.removePrefix(".")
    return this.extension().equals(cleanExtension, ignoreCase = true)
}

/**
 * Creates a directory and all parent directories if they don't exist.
 *
 * @return True if the directory was created or already exists, false otherwise.
 * @throws SecurityException If the directory cannot be created due to permissions.
 *
 * **Note**: Uses `kotlin.io.path.Path` (API 26+). Falls back to `mkdirs()` for compatibility.
 *
 * **Example**:
 * ```kotlin
 * val dir = File(context.filesDir, "images")
 * val created = dir.createDirectories()
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.createDirectories(): Boolean {
    return try {
        if (exists()) {
            isDirectory
        } else {
            Path(absolutePath).createDirectories()
            true
        }
    } catch (e: IOException) {
        mkdirs() && exists() && isDirectory
    }
}

/**
 * Safely deletes a file or directory.
 *
 * @param recursively If true and this is a directory, also deletes all contents (default: false).
 * @return True if deletion succeeds, false otherwise.
 * @throws SecurityException If the file cannot be deleted due to permissions.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("temp.txt")
 * val deleted = file?.safeDelete() ?: false
 * ```
 */
@WorkerThread
@Throws(SecurityException::class)
fun File.safeDelete(recursively: Boolean = false): Boolean {
    if (!exists()) return true
    return if (isDirectory && recursively) {
        listFiles()?.all { it.safeDelete(true) } ?: true && delete()
    } else {
        delete()
    }
}

/**
 * Checks if the file exists and is readable.
 *
 * @return True if the file exists and is readable, false otherwise.
 *
 * **Note**: Combines `exists()` and `canRead()` for convenience.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val canRead = file?.isReadable() ?: false
 * ```
 */
fun File.isReadable(): Boolean {
    return exists() && canRead()
}

/**
 * Checks if the file exists and is writable.
 *
 * @return True if the file exists and is writable, false otherwise.
 *
 * **Note**: Combines `exists()` and `canWrite()` for convenience.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val canWrite = file?.isWritable() ?: false
 * ```
 */
fun File.isWritable(): Boolean {
    return exists() && canWrite()
}

/**
 * Gets the file size in bytes.
 *
 * @return The file size in bytes, or 0 if the file doesn't exist or is a directory.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val size = file?.sizeInBytes() ?: 0L
 * ```
 */
fun File.sizeInBytes(): Long {
    return if (exists() && isFile) length() else 0L
}

/**
 * Checks if the file is empty (length = 0).
 *
 * @return True if the file exists and is empty, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = context.getInternalFile("data.txt")
 * val isEmpty = file?.isEmpty() ?: true
 * ```
 */
fun File.isEmpty(): Boolean {
    return exists() && isFile && length() == 0L
}