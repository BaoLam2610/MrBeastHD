package com.lambao.base.extension

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.annotation.WorkerThread
import androidx.core.graphics.scale
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

/**
 * Saves the Bitmap to Internal Storage and returns its Uri.
 *
 * @param context The Android context to access filesDir.
 * @param fileName The name of the file (e.g., "image.jpg").
 * @param format The compression format (default: JPEG).
 * @param quality The compression quality (0-100, default: 100).
 * @return The Uri of the saved file, or null if saving fails.
 *
 * **Note**: No permissions are required for Internal Storage.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
 * val uri = bitmap.saveToInternalStorage(context, "image.jpg")
 * ```
 */
@WorkerThread
fun Bitmap.saveToInternalStorage(
    context: Context,
    fileName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Uri? {
    return try {
        val file = context.getInternalFile(fileName) ?: return null
        FileOutputStream(file).use { output ->
            compress(format, quality, output)
        }
        Uri.fromFile(file)
    } catch (e: IOException) {
        null
    }
}

/**
 * Saves the Bitmap to Cache Directory and returns its Uri.
 *
 * @param context The Android context to access cacheDir.
 * @param fileName The name of the file (e.g., "temp_image.jpg").
 * @param format The compression format (default: JPEG).
 * @param quality The compression quality (0-100, default: 100).
 * @return The Uri of the saved file, or null if saving fails.
 *
 * **Note**: Cache files may be deleted by the system when storage is low.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
 * val uri = bitmap.saveToCache(context, "temp_image.jpg")
 * ```
 */
@WorkerThread
fun Bitmap.saveToCache(
    context: Context,
    fileName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Uri? {
    return try {
        val file = context.getCacheFile(fileName) ?: return null
        FileOutputStream(file).use { output ->
            compress(format, quality, output)
        }
        Uri.fromFile(file)
    } catch (e: IOException) {
        null
    }
}

/**
 * Saves the Bitmap to MediaStore (External Storage) and returns its Uri.
 *
 * @param context The Android context to access MediaStore.
 * @param fileName The name of the file (e.g., "image.jpg").
 * @param format The compression format (default: JPEG).
 * @param quality The compression quality (0-100, default: 100).
 * @param relativePath The relative path in Pictures directory (default: "Pictures/MyApp").
 * @return The Uri of the saved file, or null if saving fails.
 *
 * **Note**:
 * - On API 29+, uses MediaStore and requires no permissions.
 * - On API < 29, requires WRITE_EXTERNAL_STORAGE permission.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
 * val uri = bitmap.saveToMediaStore(context, "image.jpg")
 * ```
 */
@WorkerThread
fun Bitmap.saveToMediaStore(
    context: Context,
    fileName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    relativePath: String = "Pictures/${context.getAppName()}"
): Uri? {
    return try {
        // Check permission for API < 29
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !context.hasStoragePermission()) {
            return null
        }

        val contentResolver = context.contentResolver
        val mimeType = when (format) {
            Bitmap.CompressFormat.PNG -> "image/png"
            Bitmap.CompressFormat.JPEG -> "image/jpeg"
            Bitmap.CompressFormat.WEBP -> "image/webp"
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                when (format) {
                    Bitmap.CompressFormat.WEBP_LOSSY -> "image/webp"
                    Bitmap.CompressFormat.WEBP_LOSSLESS -> "image/webp"
                    else -> "image/jpeg"
                }
            } else "image/jpeg"
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            } else {
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(directory, "$relativePath/$fileName")
                file.parentFile?.mkdirs()
                put(MediaStore.Images.Media.DATA, file.absolutePath)
            }
        }

        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            contentResolver.openOutputStream(it)?.use { output ->
                compress(format, quality, output)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(it, contentValues, null, null)
            }
        }
        uri
    } catch (e: IOException) {
        null
    }
}

/**
 * Loads a Bitmap from a Uri.
 *
 * @param context The Android context to access ContentResolver.
 * @param maxWidth The maximum width of the Bitmap (default: 1024). Set to 0 to load original size.
 * @param maxHeight The maximum height of the Bitmap (default: 1024). Set to 0 to load original size.
 * @param rotateIfNeeded Whether to rotate the image based on EXIF orientation (default: true).
 * @return The Bitmap, or null if loading fails.
 *
 * **Note**: Requires a background thread due to I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val uri = Uri.parse("content://media/external/images/media/123")
 * val bitmap = uri.loadBitmap(context)
 * ```
 */
@WorkerThread
fun Uri.loadBitmap(
    context: Context,
    maxWidth: Int = 1024,
    maxHeight: Int = 1024,
    rotateIfNeeded: Boolean = true
): Bitmap? {
    return try {
        // Check read permission for content:// Uris
        if (scheme == ContentResolver.SCHEME_CONTENT && !context.hasStoragePermission()) {
            return null
        }

        context.contentResolver.openInputStream(this)?.use { input ->
            val options = BitmapFactory.Options().apply {
                // Calculate inSampleSize for resizing
                if (maxWidth > 0 && maxHeight > 0) {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeStream(input, null, this)
                    inSampleSize = calculateInSampleSize(outWidth, outHeight, maxWidth, maxHeight)
                    inJustDecodeBounds = false
                }
            }

            // Reopen input stream for actual decoding
            context.contentResolver.openInputStream(this)?.use { newInput ->
                val bitmap = BitmapFactory.decodeStream(newInput, null, options) ?: return null

                // Apply rotation if needed
                if (rotateIfNeeded) {
                    val rotationDegrees = getRotationDegrees(context)
                    if (rotationDegrees != 0) {
                        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                        return Bitmap.createBitmap(
                            bitmap,
                            0,
                            0,
                            bitmap.width,
                            bitmap.height,
                            matrix,
                            true
                        )
                    }
                }

                bitmap
            }
        }
    } catch (e: IOException) {
        null
    }
}

/**
 * Loads a Bitmap from a URL.
 *
 * @param context The Android context for caching (if saveToCache is true).
 * @param url The URL of the image (e.g., "https://example.com/image.jpg").
 * @param maxWidth The maximum width of the Bitmap (default: 1024).
 * @param maxHeight The maximum height of the Bitmap (default: 1024).
 * @param saveToCache Whether to save the downloaded image to cache (default: false).
 * @return The Bitmap, or null if loading fails.
 *
 * **Note**: Requires a background thread and INTERNET permission.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = Uri.loadBitmapFromUrl(context, "https://example.com/image.jpg")
 * ```
 */
@WorkerThread
fun Uri.loadBitmapFromUrl(
    context: Context,
    url: String,
    maxWidth: Int = 1024,
    maxHeight: Int = 1024,
    saveToCache: Boolean = false
): Bitmap? {
    return try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.doInput = true
        connection.connect()

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            return null
        }

        connection.inputStream.use { input ->
            val options = BitmapFactory.Options().apply {
                if (maxWidth > 0 && maxHeight > 0) {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeStream(input, null, this)
                    inSampleSize = calculateInSampleSize(outWidth, outHeight, maxWidth, maxHeight)
                    inJustDecodeBounds = false
                }
            }

            // Reopen stream for decoding
            val newConnection = URL(url).openConnection() as HttpURLConnection
            newConnection.inputStream.use { newInput ->
                val bitmap = BitmapFactory.decodeStream(newInput, null, options) ?: return null

                // Save to cache if requested
                if (saveToCache) {
                    val fileName = url.substringAfterLast("/").takeIf { it.isNotEmpty() }
                        ?: "${UUID.randomUUID()}.jpg"
                    bitmap.saveToCache(
                        context,
                        fileName,
                        getBitmapFormatForExtension(fileName.substringAfterLast("."))
                    )
                }

                bitmap
            }
        }
    } catch (e: IOException) {
        null
    }
}

/**
 * Saves a Bitmap loaded from a URL to MediaStore.
 *
 * @param context The Android context to access MediaStore.
 * @param url The URL of the image.
 * @param fileName The name of the file (e.g., "image.jpg").
 * @param format The compression format (default: JPEG).
 * @param quality The compression quality (0-100, default: 100).
 * @param relativePath The relative path in Pictures directory (default: "Pictures/MyApp").
 * @return The Uri of the saved file, or null if saving fails.
 *
 * **Note**: Requires INTERNET permission and WRITE_EXTERNAL_STORAGE for API < 29.
 *
 * **Example**:
 * ```kotlin
 * val uri = Uri.saveBitmapFromUrlToMediaStore(context, "https://example.com/image.jpg", "image.jpg")
 * ```
 */
@WorkerThread
fun Uri.saveBitmapFromUrlToMediaStore(
    context: Context,
    url: String,
    fileName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    relativePath: String = "Pictures/${context.getAppName()}"
): Uri? {
    val bitmap = loadBitmapFromUrl(context, url, saveToCache = false) ?: return null
    return bitmap.saveToMediaStore(context, fileName, format, quality, relativePath)
}

/**
 * Saves a document (e.g., Word, Excel, PowerPoint) to MediaStore and returns its Uri.
 *
 * @param context The Android context to access MediaStore.
 * @param fileName The name of the file (e.g., "document.docx").
 * @param relativePath The relative path in Documents directory (default: "Documents/<App name>").
 * @return The Uri of the saved file, or null if saving fails.
 *
 * **Note**:
 * - On API 29+, uses MediaStore and requires no permissions.
 * - On API < 29, requires WRITE_EXTERNAL_STORAGE permission.
 *
 * **Example**:
 * ```kotlin
 * val sourceUri = Uri.parse("content://com.example.provider/document.docx")
 * val uri = sourceUri.saveDocumentToMediaStore(context, "document.docx")
 * ```
 */
@WorkerThread
fun Uri.saveDocumentToMediaStore(
    context: Context,
    fileName: String,
    relativePath: String = "Documents/${context.getAppName()}"
): Uri? {
    return try {
        // Check permission for API < 29
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !context.hasStoragePermission()) {
            return null
        }

        val mimeType = getMimeType(context) ?: return null
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
                put(MediaStore.Downloads.IS_PENDING, 1)
            } else {
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                val file = File(directory, "$relativePath/$fileName")
                file.parentFile?.mkdirs()
                put(MediaStore.Downloads.DATA, file.absolutePath)
            }
        }

        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            contentResolver.openOutputStream(it)?.use { output ->
                contentResolver.openInputStream(this)?.use { input ->
                    input.copyTo(output)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                contentResolver.update(it, contentValues, null, null)
            }
        }
        uri
    } catch (e: IOException) {
        null
    }
}

/**
 * Saves a document to Internal Storage and returns its Uri.
 *
 * @param context The Android context to access filesDir.
 * @param fileName The name of the file (e.g., "document.docx").
 * @return The Uri of the saved file, or null if saving fails.
 *
 * **Example**:
 * ```kotlin
 * val sourceUri = Uri.parse("content://com.example.provider/document.docx")
 * val uri = sourceUri.saveDocumentToInternalStorage(context, "document.docx")
 * ```
 */
@WorkerThread
fun Uri.saveDocumentToInternalStorage(
    context: Context,
    fileName: String
): Uri? {
    return try {
        val file = context.getInternalFile(fileName) ?: return null
        context.contentResolver.openInputStream(this)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        Uri.fromFile(file)
    } catch (e: IOException) {
        null
    }
}

/**
 * Saves the Bitmap to a File.
 *
 * @param file The destination File.
 * @param format The compression format (default: JPEG).
 * @param quality The compression quality (0-100, default: 100).
 * @return True if saving succeeds, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
 * val file = File(context.filesDir, "image.jpg")
 * val success = bitmap.saveToFile(file)
 * ```
 */
@WorkerThread
fun Bitmap.saveToFile(
    file: File,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Boolean {
    return try {
        file.parentFile?.mkdirs()
        FileOutputStream(file).use { output ->
            compress(format, quality, output)
        }
        true
    } catch (e: IOException) {
        false
    }
}

/**
 * Gets the file path from a Uri.
 *
 * @param context The Android context to access ContentResolver.
 * @return The file path, or null if the Uri is not a file or cannot be resolved.
 *
 * **Note**: Works for file:// and content:// Uris. Requires a background thread.
 *
 * **Example**:
 * ```kotlin
 * val uri = Uri.parse("content://media/external/images/media/123")
 * val path = uri.getFilePath(context)
 * ```
 */
@WorkerThread
fun Uri.getFilePath(context: Context): String? {
    return try {
        when (scheme) {
            ContentResolver.SCHEME_FILE -> path
            ContentResolver.SCHEME_CONTENT -> {
                // Try MediaStore columns first
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                context.contentResolver.query(this, projection, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                        if (columnIndex != -1) {
                            return cursor.getString(columnIndex)
                        }
                    }
                }
                null // For non-MediaStore content URIs
            }

            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Gets the file size from a Uri.
 *
 * @param context The Android context to access ContentResolver.
 * @return The file size in bytes, or 0 if the size cannot be determined.
 *
 * **Note**: Requires a background thread due to I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val uri = Uri.parse("content://media/external/images/media/123")
 * val size = uri.getFileSize(context)
 * ```
 */
@WorkerThread
fun Uri.getFileSize(context: Context): Long {
    return try {
        when (scheme) {
            ContentResolver.SCHEME_CONTENT -> {
                context.contentResolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)
                    ?.use {
                        if (it.moveToFirst()) {
                            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                            if (sizeIndex != -1) {
                                return it.getLong(sizeIndex)
                            }
                        }
                    }
                context.contentResolver.openInputStream(this)?.use { input ->
                    input.available().toLong()
                } ?: 0L
            }

            ContentResolver.SCHEME_FILE -> path?.let { File(it).length() } ?: 0L
            else -> 0L
        }
    } catch (e: Exception) {
        0L
    }
}

/**
 * Gets the file name from a Uri.
 *
 * @param context The Android context to access ContentResolver.
 * @return The file name, or null if it cannot be determined.
 *
 * **Example**:
 * ```kotlin
 * val uri = Uri.parse("content://media/external/images/media/123")
 * val name = uri.getFileName(context)
 * ```
 */
@WorkerThread
fun Uri.getFileName(context: Context): String? {
    return try {
        when (scheme) {
            ContentResolver.SCHEME_FILE -> path?.let { File(it).name }
            ContentResolver.SCHEME_CONTENT -> {
                context.contentResolver.query(
                    this,
                    arrayOf(OpenableColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            return it.getString(nameIndex)
                        }
                    }
                }
                lastPathSegment
            }

            else -> lastPathSegment
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Gets the MIME type from a Uri.
 *
 * @param context The Android context to access ContentResolver.
 * @return The MIME type, or null if it cannot be determined.
 *
 * **Example**:
 * ```kotlin
 * val uri = Uri.parse("content://media/external/images/media/123")
 * val mimeType = uri.getMimeType(context)
 * ```
 */
fun Uri.getMimeType(context: Context): String? {
    return try {
        when (scheme) {
            ContentResolver.SCHEME_CONTENT -> context.contentResolver.getType(this)
            ContentResolver.SCHEME_FILE -> {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(toString())
                )
            }

            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Resizes a Bitmap to the specified width and height.
 *
 * @param width The target width in pixels.
 * @param height The target height in pixels.
 * @param filter Whether to apply filtering when scaling the bitmap.
 * @return The resized Bitmap, or null if resizing fails.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = BitmapFactory.decodeFile(path)
 * val resized = bitmap.resize(500, 500)
 * ```
 */
fun Bitmap.resize(width: Int, height: Int, filter: Boolean = true): Bitmap? {
    return try {
        this.scale(width, height, filter)
    } catch (e: Exception) {
        null
    }
}

/**
 * Scales a Bitmap while maintaining aspect ratio.
 *
 * @param maxWidth The maximum width in pixels.
 * @param maxHeight The maximum height in pixels.
 * @param filter Whether to apply filtering when scaling the bitmap.
 * @return The scaled Bitmap, or the original if no scaling is needed.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = BitmapFactory.decodeFile(path)
 * val scaled = bitmap.scaleToFit(800, 600)
 * ```
 */
fun Bitmap.scaleToFit(maxWidth: Int, maxHeight: Int, filter: Boolean = true): Bitmap {
    if (width <= maxWidth && height <= maxHeight) {
        return this
    }

    val widthRatio = maxWidth.toFloat() / width
    val heightRatio = maxHeight.toFloat() / height
    val ratio = minOf(widthRatio, heightRatio)

    val newWidth = (width * ratio).toInt()
    val newHeight = (height * ratio).toInt()

    return this.scale(newWidth, newHeight, filter)
}

/**
 * Rotates a Bitmap by the specified degrees.
 *
 * @param degrees The rotation degrees (e.g., 90, 180, 270).
 * @return The rotated Bitmap, or null if rotation fails.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = BitmapFactory.decodeFile(path)
 * val rotated = bitmap.rotate(90)
 * ```
 */
fun Bitmap.rotate(degrees: Float): Bitmap? {
    return try {
        val matrix = Matrix().apply { postRotate(degrees) }
        Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    } catch (e: Exception) {
        null
    }
}

/**
 * Crops a Bitmap to the specified rectangle.
 *
 * @param x The x coordinate of the top-left corner of the crop rectangle.
 * @param y The y coordinate of the top-left corner of the crop rectangle.
 * @param width The width of the crop rectangle.
 * @param height The height of the crop rectangle.
 * @return The cropped Bitmap, or null if cropping fails.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = BitmapFactory.decodeFile(path)
 * val cropped = bitmap.crop(0, 0, 100, 100)
 * ```
 */
fun Bitmap.crop(x: Int, y: Int, width: Int, height: Int): Bitmap? {
    return try {
        Bitmap.createBitmap(
            this,
            x,
            y,
            minOf(width, this.width - x),
            minOf(height, this.height - y)
        )
    } catch (e: Exception) {
        null
    }
}

/**
 * Converts a Bitmap to a byte array.
 *
 * @param format The compression format (default: JPEG).
 * @param quality The compression quality (0-100, default: 100).
 * @return The byte array representation of the Bitmap.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = BitmapFactory.decodeFile(path)
 * val bytes = bitmap.toByteArray()
 * ```
 */
fun Bitmap.toByteArray(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(format, quality, stream)
    return stream.toByteArray()
}

/**
 * Copies a file from one Uri to another.
 *
 * @param context The Android context to access ContentResolver.
 * @param target The target Uri or File.
 * @param bufferSize The size of the buffer used for copying (default: 8192).
 * @return The target Uri, or null if copying fails.
 *
 * **Example**:
 * ```kotlin
 * val sourceUri = Uri.parse("content://media/external/images/media/123")
 * val targetFile = context.getInternalFile("image_copy.jpg")
 * val targetUri = sourceUri.copyTo(context, targetFile)
 * ```
 */
@WorkerThread
fun Uri.copyTo(context: Context, target: Any, bufferSize: Int = 8192): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this) ?: return null
        val outputStream: OutputStream = when (target) {
            is Uri -> context.contentResolver.openOutputStream(target) ?: return null
            is File -> {
                target.parentFile?.mkdirs()
                FileOutputStream(target)
            }

            else -> return null
        }

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output, bufferSize)
            }
        }

        when (target) {
            is Uri -> target
            is File -> Uri.fromFile(target)
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Suspending version of loadBitmap for use with Kotlin coroutines.
 *
 * @param context The Android context to access ContentResolver.
 * @param maxWidth The maximum width of the Bitmap (default: 1024).
 * @param maxHeight The maximum height of the Bitmap (default: 1024).
 * @param rotateIfNeeded Whether to rotate the image based on EXIF orientation (default: true).
 * @return The Bitmap, or null if loading fails.
 *
 * **Example**:
 * ```kotlin
 * val bitmap = uri.loadBitmapSuspend(context)
 * ```
 */
suspend fun Uri.loadBitmapSuspend(
    context: Context,
    maxWidth: Int = 1024,
    maxHeight: Int = 1024,
    rotateIfNeeded: Boolean = true
): Bitmap? = withContext(Dispatchers.IO) {
    loadBitmap(context, maxWidth, maxHeight, rotateIfNeeded)
}

/**
 * Gets the appropriate bitmap format for a file extension.
 *
 * @param extension The file extension (e.g., "jpg", "png").
 * @return The appropriate Bitmap.CompressFormat.
 */
fun getBitmapFormatForExtension(extension: String): Bitmap.CompressFormat {
    return when (extension.lowercase()) {
        "png" -> Bitmap.CompressFormat.PNG
        "webp" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            @Suppress("DEPRECATION")
            Bitmap.CompressFormat.WEBP
        }

        else -> Bitmap.CompressFormat.JPEG
    }
}

/**
 * Determines the image type from a Uri.
 *
 * @param context The Android context to access ContentResolver.
 * @return The appropriate Bitmap.CompressFormat, or JPEG if type cannot be determined.
 */
fun Uri.getBitmapFormat(context: Context): Bitmap.CompressFormat {
    val mimeType = getMimeType(context)?.lowercase()
    return when {
        mimeType?.contains("png") == true -> Bitmap.CompressFormat.PNG
        mimeType?.contains("webp") == true -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            @Suppress("DEPRECATION")
            Bitmap.CompressFormat.WEBP
        }

        else -> Bitmap.CompressFormat.JPEG
    }
}

/**
 * Calculates the inSampleSize for resizing a Bitmap.
 *
 * @param width The original width of the Bitmap.
 * @param height The original height of the Bitmap.
 * @param reqWidth The requested maximum width.
 * @param reqHeight The requested maximum height.
 * @return The inSampleSize value for BitmapFactory.Options.
 */
private fun calculateInSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
    var inSampleSize = 1
    if (width > reqWidth || height > reqHeight) {
        val halfWidth = width / 2
        val halfHeight = height / 2
        while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

/**
 * Gets the rotation degrees from EXIF data in a Uri.
 *
 * @param context The Android context to access ContentResolver.
 * @return The rotation in degrees (0, 90, 180, or 270).
 */
private fun Uri.getRotationDegrees(context: Context): Int {
    return try {
        if (ContentResolver.SCHEME_FILE == scheme) {
            path?.let { filePath -> return getExifOrientation(filePath) }
        }

        context.contentResolver.openInputStream(this)?.use { input ->
            val exif = ExifInterface(input)
            return when (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }

        0
    } catch (e: Exception) {
        0
    }
}

/**
 * Gets the EXIF orientation from a file path.
 *
 * @param filePath The path to the image file.
 * @return The rotation in degrees (0, 90, 180, or 270).
 */
private fun getExifOrientation(filePath: String): Int {
    return try {
        val exif = ExifInterface(filePath)
        when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    } catch (e: Exception) {
        0
    }
}