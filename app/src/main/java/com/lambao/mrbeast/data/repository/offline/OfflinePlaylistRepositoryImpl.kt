package com.lambao.mrbeast.data.repository.offline

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.lambao.base.data.Resource
import com.lambao.base.data.local.BaseLocalDataSource
import com.lambao.base.data.local.LocalDataException
import com.lambao.base.data.local.LocalErrorType
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.mrbeast.data.local.model.SongLocalDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflinePlaylistRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    dispatcherProvider: DispatcherProvider
) : BaseLocalDataSource(dispatcherProvider), OfflinePlaylistRepository {

    override fun getPlaylist(): Flow<Resource<List<SongLocalDto>>> = safeCall {
        fetchLocalSongs()
    }

    private suspend fun fetchLocalSongs(): List<SongLocalDto> {
        val songList = mutableListOf<SongLocalDto>()
        val contentResolver = context.contentResolver

        val audioProjection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            audioProjection,
            MediaStore.Audio.Media.IS_MUSIC + " != 0",
            null,
            MediaStore.Audio.Media.TITLE + " ASC"
        ) ?: throw LocalDataException(
            type = LocalErrorType.STORAGE_UNAVAILABLE,
            message = "Unable to access media store"
        )

        cursor.use { c ->
            val idColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val pathColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val sizeColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val albumIdColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (c.moveToNext()) {
                val albumId = c.getLong(albumIdColumn)
                val thumbnail = getAlbumArtBitmap(albumId)

                val song = SongLocalDto(
                    id = c.getLong(idColumn).toString(),
                    title = c.getString(titleColumn),
                    artist = c.getString(artistColumn),
                    album = c.getString(albumColumn),
                    duration = c.getLong(durationColumn).takeIf { it > 0 },
                    filePath = c.getString(pathColumn),
                    fileSize = c.getLong(sizeColumn).takeIf { it > 0 },
                    thumbnail = thumbnail
                )
                songList.add(song)
            }
        }

        return songList
    }

    private suspend fun getAlbumArtBitmap(albumId: Long): Bitmap? =
        withContext(coroutineDispatcher) {
            try {
                val albumUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // API 29+: Dùng loadThumbnail
                    context.contentResolver.loadThumbnail(
                        albumUri,
                        Size(300, 300), // Kích thước mặc định
                        null
                    )
                } else {
                    // API < 29: Dùng ALBUM_ART
                    val cursor = context.contentResolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Audio.Albums.ALBUM_ART),
                        MediaStore.Audio.Albums._ID + "=?",
                        arrayOf(albumId.toString()),
                        null
                    )
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val artPath =
                                it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
                            artPath?.let { path ->
                                BitmapFactory.decodeFile(path)
                            }
                        } else null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}