package com.lambao.base.extension

import android.content.Context
import android.webkit.MimeTypeMap
import androidx.annotation.WorkerThread
import java.io.File

/**
 * Enum representing common file types, including media and document types.
 */
enum class MediaFileType {
    IMAGE, PDF, VIDEO, TEXT, WORD, EXCEL, POWERPOINT, OTHER
}

/**
 * List of common text file extensions (case-insensitive).
 */
private val TEXT_EXTENSIONS = setOf("txt", "csv", "log", "md", "json", "xml")

/**
 * List of Microsoft Word file extensions (case-insensitive).
 */
private val WORD_EXTENSIONS = setOf("doc", "docx")

/**
 * List of Microsoft Excel file extensions (case-insensitive).
 */
private val EXCEL_EXTENSIONS = setOf("xls", "xlsx", "csv")

/**
 * List of Microsoft PowerPoint file extensions (case-insensitive).
 */
private val POWERPOINT_EXTENSIONS = setOf("ppt", "pptx")

/**
 * List of common image file extensions (case-insensitive).
 */
private val IMAGE_EXTENSIONS = setOf("jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff")

/**
 * List of PDF file extensions (case-insensitive).
 */
private val PDF_EXTENSIONS = setOf("pdf")

/**
 * List of common video file extensions (case-insensitive).
 */
private val VIDEO_EXTENSIONS = setOf("mp4", "avi", "mkv", "mov", "wmv", "flv", "3gp", "mpeg", "webm")

/**
 * Checks if the file is a text file based on its extension.
 *
 * @param extensions Optional set of text extensions to check (default: txt, csv, log, md, json, xml).
 * @return True if the file has a text extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "notes.txt")
 * val isText = file.isText() // true
 * ```
 */
fun File.isText(extensions: Set<String> = TEXT_EXTENSIONS): Boolean {
    return extension.lowercase() in extensions
}

/**
 * Checks if the file is a Microsoft Word document based on its extension.
 *
 * @param extensions Optional set of Word extensions to check (default: doc, docx).
 * @return True if the file has a Word extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "report.docx")
 * val isWord = file.isWord() // true
 * ```
 */
fun File.isWord(extensions: Set<String> = WORD_EXTENSIONS): Boolean {
    return extension.lowercase() in extensions
}

/**
 * Checks if the file is a Microsoft Excel spreadsheet based on its extension.
 *
 * @param extensions Optional set of Excel extensions to check (default: xls, xlsx, csv).
 * @return True if the file has an Excel extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "data.xlsx")
 * val isExcel = file.isExcel() // true
 * ```
 */
fun File.isExcel(extensions: Set<String> = EXCEL_EXTENSIONS): Boolean {
    return extension.lowercase() in extensions
}

/**
 * Checks if the file is a Microsoft PowerPoint presentation based on its extension.
 *
 * @param extensions Optional set of PowerPoint extensions to check (default: ppt, pptx).
 * @return True if the file has a PowerPoint extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "slides.pptx")
 * val isPowerPoint = file.isPowerPoint() // true
 * ```
 */
fun File.isPowerPoint(extensions: Set<String> = POWERPOINT_EXTENSIONS): Boolean {
    return extension.lowercase() in extensions
}

/**
 * Checks if the file is a text file based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return True if the file's MIME type starts with "text/" or matches common text MIME types (e.g., "application/json", "application/xml"), false otherwise.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "notes.txt")
 * val isText = file.isTextByMimeType(context) // true
 * ```
 */
@WorkerThread
fun File.isTextByMimeType(context: Context): Boolean {
    val mimeType = getMimeType(context) ?: return false
    return mimeType.startsWith("text/") ||
            mimeType in setOf("application/json", "application/xml", "text/csv")
}

/**
 * Checks if the file is a Microsoft Word document based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return True if the file's MIME type matches Word MIME types, false otherwise.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "report.docx")
 * val isWord = file.isWordByMimeType(context) // true
 * ```
 */
@WorkerThread
fun File.isWordByMimeType(context: Context): Boolean {
    val mimeType = getMimeType(context) ?: return false
    return mimeType in setOf(
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    )
}

/**
 * Checks if the file is a Microsoft Excel spreadsheet based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return True if the file's MIME type matches Excel MIME types, false otherwise.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "data.xlsx")
 * val isExcel = file.isExcelByMimeType(context) // true
 * ```
 */
@WorkerThread
fun File.isExcelByMimeType(context: Context): Boolean {
    val mimeType = getMimeType(context) ?: return false
    return mimeType in setOf(
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "text/csv"
    )
}

/**
 * Checks if the file is a Microsoft PowerPoint presentation based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return True if the file's MIME type matches PowerPoint MIME types, false otherwise.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "slides.pptx")
 * val isPowerPoint = file.isPowerPointByMimeType(context) // true
 * ```
 */
@WorkerThread
fun File.isPowerPointByMimeType(context: Context): Boolean {
    val mimeType = getMimeType(context) ?: return false
    return mimeType in setOf(
        "application/vnd.ms-powerpoint",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    )
}

/**
 * Checks if the file is an image based on its extension.
 *
 * @param extensions Optional set of image extensions to check (default: jpg, png, etc.).
 * @return True if the file has an image extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "photo.jpg")
 * val isImage = file.isImage() // true
 * ```
 */
fun File.isImage(extensions: Set<String> = IMAGE_EXTENSIONS): Boolean {
    return extension.lowercase() in extensions
}

/**
 * Checks if the file is a PDF based on its extension.
 *
 * @return True if the file has a PDF extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "document.pdf")
 * val isPdf = file.isPdf() // true
 * ```
 */
fun File.isPdf(): Boolean {
    return extension.lowercase() in PDF_EXTENSIONS
}

/**
 * Checks if the file is a video based on its extension.
 *
 * @param extensions Optional set of video extensions to check (default: mp4, avi, etc.).
 * @return True if the file has a video extension, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "movie.mp4")
 * val isVideo = file.isVideo() // true
 * ```
 */
fun File.isVideo(extensions: Set<String> = VIDEO_EXTENSIONS): Boolean {
    return extension.lowercase() in extensions
}

/**
 * Checks if the file is an image based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return True if the file's MIME type starts with "image/", false otherwise.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "photo.jpg")
 * val isImage = file.isImageByMimeType(context) // true
 * ```
 */
@WorkerThread
fun File.isImageByMimeType(context: Context): Boolean {
    val mimeType = getMimeType(context) ?: return false
    return mimeType.startsWith("image/")
}

/**
 * Checks if the file is a PDF based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return True if the file's MIME type is "application/pdf", false otherwise.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "document.pdf")
 * val isPdf = file.isPdfByMimeType(context) // true
 * ```
 */
@WorkerThread
fun File.isPdfByMimeType(context: Context): Boolean {
    val mimeType = getMimeType(context) ?: return false
    return mimeType == "application/pdf"
}

/**
 * Checks if the file is a video based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return True if the file's MIME type starts with "video/", false otherwise.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "movie.mp4")
 * val isVideo = file.isVideoByMimeType(context) // true
 * ```
 */
@WorkerThread
fun File.isVideoByMimeType(context: Context): Boolean {
    val mimeType = getMimeType(context) ?: return false
    return mimeType.startsWith("video/")
}

/**
 * Gets the MIME type of the file using its extension.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return The MIME type (e.g., "text/plain", "application/vnd.ms-excel"), or null if unknown.
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "data.xlsx")
 * val mimeType = file.getMimeType(context) // "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
 * ```
 */
@WorkerThread
fun File.getMimeType(context: Context): String? {
    val extension = extension.lowercase()
    return if (extension.isNotEmpty()) {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    } else {
        null
    }
}

/**
 * Determines the file type based on its extension.
 *
 * @param textExtensions Optional set of text extensions (default: txt, csv, log, md, json, xml).
 * @param wordExtensions Optional set of Word extensions (default: doc, docx).
 * @param excelExtensions Optional set of Excel extensions (default: xls, xlsx, csv).
 * @param powerPointExtensions Optional set of PowerPoint extensions (default: ppt, pptx).
 * @param imageExtensions Optional set of image extensions (default: jpg, png, etc.).
 * @param videoExtensions Optional set of video extensions (default: mp4, avi, etc.).
 * @return The [MediaFileType] (TEXT, WORD, EXCEL, POWERPOINT, IMAGE, PDF, VIDEO, or OTHER).
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "report.docx")
 * val type = file.getDocumentFileType() // MediaFileType.WORD
 * ```
 */
fun File.getDocumentFileType(
    textExtensions: Set<String> = TEXT_EXTENSIONS,
    wordExtensions: Set<String> = WORD_EXTENSIONS,
    excelExtensions: Set<String> = EXCEL_EXTENSIONS,
    powerPointExtensions: Set<String> = POWERPOINT_EXTENSIONS,
    imageExtensions: Set<String> = IMAGE_EXTENSIONS,
    videoExtensions: Set<String> = VIDEO_EXTENSIONS
): MediaFileType {
    return when {
        isText(textExtensions) -> MediaFileType.TEXT
        isWord(wordExtensions) -> MediaFileType.WORD
        isExcel(excelExtensions) -> MediaFileType.EXCEL
        isPowerPoint(powerPointExtensions) -> MediaFileType.POWERPOINT
        isImage(imageExtensions) -> MediaFileType.IMAGE
        isPdf() -> MediaFileType.PDF
        isVideo(videoExtensions) -> MediaFileType.VIDEO
        else -> MediaFileType.OTHER
    }
}

/**
 * Determines the file type based on its MIME type.
 *
 * @param context The Android context to access MimeTypeMap.
 * @return The [MediaFileType] (TEXT, WORD, EXCEL, POWERPOINT, IMAGE, PDF, VIDEO, or OTHER).
 *
 * **Note**: Requires a background thread due to potential I/O operations.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "slides.pptx")
 * val type = file.getDocumentFileTypeByMimeType(context) // MediaFileType.POWERPOINT
 * ```
 */
@WorkerThread
fun File.getDocumentFileTypeByMimeType(context: Context): MediaFileType {
    return when {
        isTextByMimeType(context) -> MediaFileType.TEXT
        isWordByMimeType(context) -> MediaFileType.WORD
        isExcelByMimeType(context) -> MediaFileType.EXCEL
        isPowerPointByMimeType(context) -> MediaFileType.POWERPOINT
        isImageByMimeType(context) -> MediaFileType.IMAGE
        isPdfByMimeType(context) -> MediaFileType.PDF
        isVideoByMimeType(context) -> MediaFileType.VIDEO
        else -> MediaFileType.OTHER
    }
}

/**
 * Checks if the file has one of the specified extensions (case-insensitive).
 *
 * @param extensions The set of extensions to check (e.g., setOf("txt", "csv")).
 * @return True if the file's extension is in the set, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val file = File(context.filesDir, "notes.txt")
 * val isTxtOrCsv = file.hasAnyExtension(setOf("txt", "csv")) // true
 * ```
 */
fun File.hasAnyExtension(extensions: Set<String>): Boolean {
    return extension.lowercase() in extensions
}