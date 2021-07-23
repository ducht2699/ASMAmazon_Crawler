package com.vnsoft.exam.utils.extension


import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.util.Size
import android.webkit.MimeTypeMap
import java.io.*
import java.text.DecimalFormat


const val TAG = "FileUtils"
private const val DEBUG = false // Set to true to enable logging


const val MIME_TYPE_AUDIO = "audio/*"
const val MIME_TYPE_TEXT = "text/*"
const val MIME_TYPE_IMAGE = "image/*"
const val MIME_TYPE_VIDEO = "video/*"
const val MIME_TYPE_APP = "application/*"
const val AUTHORITY = "vn.vnsoft.news.utils.extension.documents"


const val HIDDEN_PREFIX = "."

/**
 * Gets the extension of a file name, like ".png" or ".jpg".
 *
 * @param uri
 * @return Extension including the dot("."); "" if there is no extension;
 * null if uri was null.
 */
fun getExtension(uri: String?): String? {
    if (uri == null) {
        return null
    }
    val dot = uri.lastIndexOf(".")
    return if (dot >= 0) {
        uri.substring(dot)
    } else {
        // No extension.
        ""
    }
}

/**
 * @return Whether the URI is a local one.
 */
fun isLocal(url: String?): Boolean {
    return url != null && !url.startsWith("http://") && !url.startsWith("https://")
}

/**
 * @return True if Uri is a MediaStore Uri.
 * @author paulburke
 */
fun isMediaUri(uri: Uri?): Boolean {
    return "media".equals(uri?.getAuthority(), ignoreCase = true)
}

/**
 * Convert File into Uri.
 *
 * @param file
 * @return uri
 */
fun getUri(file: File?): Uri? {
    return if (file != null) {
        Uri.fromFile(file)
    } else null
}

/**
 * Returns the path only (without file name).
 *
 * @param file
 * @return
 */
fun getPathWithoutFilename(file: File?): File? {
    return if (file != null) {
        if (file.isDirectory()) {
            // no file to be split off. Return everything
            file
        } else {
            val filename: String = file.getName()
            val filepath: String = file.getAbsolutePath()

            // Construct path without file name.
            var pathwithoutname = filepath.substring(0,
                    filepath.length - filename.length)
            if (pathwithoutname.endsWith("/")) {
                pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
            }
            File(pathwithoutname)
        }
    } else null
}

/**
 * @return The MIME type for the given file.
 */
fun getMimeType(file: File): String? {
    val extension = getExtension(file.getName())
    return if (extension!!.length > 0) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1)) else "application/octet-stream"
}

/**
 * @return The MIME type for the give Uri.
 */
fun getMimeType(context: Context, uri: Uri): String? {
    val file = File(getPath(context, uri))
    return getMimeType(file)
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is [LocalStorageProvider].
 * @author paulburke
 */
fun isLocalStorageDocument(uri: Uri): Boolean {
    return AUTHORITY == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 * @author paulburke
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @author paulburke
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @author paulburke
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.getAuthority()
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context The context.
 * @param uri The Uri to query.
 * @param selection (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 * @author paulburke
 */
fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                  selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
            column
    )
    try {
        cursor = context.getContentResolver().query(uri!!, projection, selection, selectionArgs,
                null)
        if (cursor != null && cursor.moveToFirst()) {
            if (DEBUG) DatabaseUtils.dumpCursor(cursor)
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        if (cursor != null) cursor.close()
    }
    return null
}

/**
 * Get a file path from a Uri. This will get the the path for Storage Access
 * Framework Documents, as well as the _data field for the MediaStore and
 * other file-based ContentProviders.<br></br>
 * <br></br>
 * Callers should check whether the path is local before assuming it
 * represents a local file.
 *
 * @param context The context.
 * @param uri The Uri to query.
 * @see .isLocal
 * @see .getFile
 * @author paulburke
 */
fun getPath(context: Context, uri: Uri): String? {
    if (DEBUG) Log.d("$TAG File -",
            "Authority: " + uri.getAuthority().toString() +
                    ", Fragment: " + uri.getFragment().toString() +
                    ", Port: " + uri.getPort().toString() +
                    ", Query: " + uri.getQuery().toString() +
                    ", Scheme: " + uri.getScheme().toString() +
                    ", Host: " + uri.getHost().toString() +
                    ", Segments: " + uri.getPathSegments().toString()
    )
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // LocalStorageProvider
        if (isLocalStorageDocument(uri)) {
            // The path is the id
            return DocumentsContract.getDocumentId(uri)
        } else
            if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                    split[1]
            )
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {

        // Return the remote address
        return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
        return uri.getPath()
    }
    return null
}

/**
 * Convert Uri into File, if possible.
 *
 * @return file A local file that the Uri was pointing to, or null if the
 * Uri is unsupported or pointed to a remote resource.
 * @see .getPath
 * @author paulburke
 */
fun getFile(context: Context?, uri: Uri?): File? {
    if (isGoogleDrive(uri!!)) // check if file selected from google drive
    {
        return saveFileIntoExternalStorageByUri(context!!, uri)
    }
    val path = uri.path
    if (path != null && isLocal(path)) {
        return File(path)
    }
    return null
}

/**
 * Get the file size in a human-readable string.
 *
 * @param size
 * @return
 * @author paulburke
 */
fun getReadableFileSize(size: Int): String? {
    val BYTES_IN_KILOBYTES = 1024
    val dec = DecimalFormat("###.#")
    val KILOBYTES = " KB"
    val MEGABYTES = " MB"
    val GIGABYTES = " GB"
    var fileSize = 0f
    var suffix = KILOBYTES
    if (size > BYTES_IN_KILOBYTES) {
        fileSize = size / BYTES_IN_KILOBYTES.toFloat()
        if (fileSize > BYTES_IN_KILOBYTES) {
            fileSize = fileSize / BYTES_IN_KILOBYTES
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES
                suffix = GIGABYTES
            } else {
                suffix = MEGABYTES
            }
        }
    }
    return (dec.format(fileSize).toString() + suffix).toString()
}

/**
 * Attempt to retrieve the thumbnail of given File from the MediaStore. This
 * should not be called on the UI thread.
 *
 * @param context
 * @param file
 * @return
 * @author paulburke
 */
fun getThumbnail(context: Context, file: File): Bitmap? {
    return getThumbnail(context, getUri(file), getMimeType(file))
}

/**
 * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
 * should not be called on the UI thread.
 *
 * @param context
 * @param uri
 * @return
 * @author paulburke
 */
fun getThumbnail(context: Context, uri: Uri): Bitmap? {
    return getThumbnail(context, uri, getMimeType(context, uri))
}

/**
 * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
 * should not be called on the UI thread.
 *
 * @param context
 * @param uri
 * @param mimeType
 * @return
 * @author paulburke
 */
fun getThumbnail(context: Context, uri: Uri?, mimeType: String?): Bitmap? {
    if (DEBUG) Log.d(TAG, "Attempting to get thumbnail")
    if (!isMediaUri(uri)) {
        Log.e(TAG, "You can only retrieve thumbnails for images and videos.")
        return null
    }
    var bm: Bitmap? = null
    if (uri != null) {
        val resolver: ContentResolver = context.getContentResolver()
        var cursor: Cursor? = null
        try {
            cursor = resolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val id: Int = cursor.getInt(0)
                if (DEBUG) Log.d(TAG, "Got thumb ID: $id")
                if (mimeType!!.contains("video")) {
                    bm = MediaStore.Video.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Video.Thumbnails.MINI_KIND,
                            null)
                } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
                    bm = MediaStore.Images.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            null)
                }
            }
        } catch (e: Exception) {
            if (DEBUG) Log.e(TAG, "getThumbnail", e)
        } finally {
            if (cursor != null) cursor.close()
        }
    }
    return bm
}

/**
 * File and folder comparator. TODO Expose sorting option method
 *
 * @author paulburke
 */

/**
 * File (not directories) filter.
 *
 * @author paulburke
 */
var sFileFilter: FileFilter = object : FileFilter {
    override fun accept(file: File): Boolean {
        val fileName: String = file.getName()
        // Return files only (not directories) and skip hidden files
        return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX)
    }
}

/**
 * Folder (directories) filter.
 *
 * @author paulburke
 */
var sDirFilter: FileFilter = object : FileFilter {
    override fun accept(file: File): Boolean {
        val fileName: String = file.getName()
        // Return directories only and skip hidden directories
        return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX)
    }
}

/**
 * Get the Intent for selecting content to be used in an Intent Chooser.
 *
 * @return The intent for opening a file with Intent.createChooser()
 * @author paulburke
 */
fun createGetContentIntent(): Intent? {
    // Implicitly allow the user to select a particular kind of data
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    // The MIME data type filter
    intent.type = "*/*"
    // Only return URIs that can be opened with ContentResolver
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    return intent
}


fun getFileFromUri(context: Context?, uri: Uri): File? {
    return if (isGoogleDrive(uri)) // check if file selected from google drive
    {
        saveFileIntoExternalStorageByUri(context!!, uri)
    } else  // do your other calculation for the other files and return that file
        null
}


fun isGoogleDrive(uri: Uri): Boolean {
    return "com.google.android.apps.docs.storage.legacy" == uri.authority
}

fun saveFileIntoExternalStorageByUri(context: Context, uri: Uri): File? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val originalSize: Int = inputStream!!.available()
    var bis: BufferedInputStream? = null
    var bos: BufferedOutputStream? = null
    val fileName = getFileName(context, uri)
    val file = makeEmptyFileIntoExternalStorageWithTitle(fileName)
    bis = BufferedInputStream(inputStream)
    bos = BufferedOutputStream(FileOutputStream(
            file, false))
    val buf = ByteArray(originalSize)
    bis.read(buf)
    do {
        bos.write(buf)
    } while (bis.read(buf) !== -1)
    bos.flush()
    bos.close()
    bis.close()
    return file
}

fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor!!.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}


fun makeEmptyFileIntoExternalStorageWithTitle(title: String?): File {
    val root = Environment.getExternalStorageDirectory().absolutePath
    return File(root, title)
}

fun convertBitmapToFile(context: Context, bitmap: Bitmap, size: Int): File {
    val out: Bitmap
    out = if (bitmap.height > size || bitmap.width > size) {
        val max = Math.max(bitmap.height, bitmap.width)
        val ratio = size.toFloat() / max.toFloat()
        Bitmap.createScaledBitmap(bitmap,
                (bitmap.width * ratio).toInt(),
                (bitmap.height * ratio).toInt(),
                true)
    } else {
        bitmap
    }
    val file = File(context.cacheDir, "thumb.jpeg")
    val fOut: FileOutputStream
    try {
        fOut = FileOutputStream(file)
        out.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        fOut.flush()
        fOut.close()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return file
}

fun isVideoFile(path: String?): Boolean {
    return path!!.contains("mp4")
}

fun getThumbByUri(path: String): Bitmap? {
//    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//    val cursor = context.contentResolver.query(selectedImageUri, filePathColumn, null, null, null)
//    cursor!!.moveToFirst()
//
//    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//    val picturePath = cursor.getString(columnIndex)
//    cursor.close()

    return if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q) {
        ThumbnailUtils.createVideoThumbnail(File(path), Size(500, 500), CancellationSignal())
    } else{
        ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)
    }

}

fun getUriFromPath(filePath: String): Uri{
    return if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
        Uri.parse(filePath);
    } else{
        Uri.fromFile(File(filePath));
    }
}

fun getBitmapFormPath(path: String): Bitmap {
    var image = File(path)
    val bmOptions = BitmapFactory.Options()
    return BitmapFactory.decodeFile(image.absolutePath, bmOptions)
}